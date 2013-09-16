/**
 * 
 */
package sandbox.quickstart.service.impl;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import sandbox.quickstart.entity.ECandidateBuilding;
import sandbox.quickstart.entity.ECandidateBuilding_;
import sandbox.quickstart.entity.EUser;
import sandbox.quickstart.entity.EUser_;
import sandbox.quickstart.model.LoginUser;
import sandbox.quickstart.service.IBuildingService;
import sandbox.quickstart.service.IUserService;

/**
 * @author jabaraster
 */
public class BuildingServiceImpl extends JpaDaoBase implements IBuildingService {
    private static final long  serialVersionUID = -2412798534052597835L;

    private final IUserService userService;

    /**
     * @param pEntityManagerFactory -
     * @param pUserService -
     */
    @Inject
    public BuildingServiceImpl(final EntityManagerFactory pEntityManagerFactory, final IUserService pUserService) {
        super(pEntityManagerFactory);
        this.userService = pUserService;
    }

    /**
     * @see sandbox.quickstart.service.IBuildingService#count(SearchCondition)
     */
    @Override
    public long count(final SearchCondition pCondition) {
        ArgUtil.checkNull(pCondition, "pCondition"); //$NON-NLS-1$

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<ECandidateBuilding> root = query.from(ECandidateBuilding.class);

        query.select(builder.count(root));

        final List<Predicate> ps = new ArrayList<>();
        addSearchPredicates(ps, pCondition, builder, root);
        query.where(ps.toArray(new Predicate[ps.size()]));

        try {
            return getSingleResult(em.createQuery(query)).longValue();
        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @see sandbox.quickstart.service.IBuildingService#insert(sandbox.quickstart.model.LoginUser, sandbox.quickstart.entity.ECandidateBuilding,
     *      java.io.InputStream)
     */
    @Override
    public void insert(final LoginUser pLoginUser, final ECandidateBuilding pNewBuilding, final InputStream pImageData) {
        ArgUtil.checkNull(pLoginUser, "pLoginUser"); //$NON-NLS-1$
        ArgUtil.checkNull(pNewBuilding, "pNewBuilding"); //$NON-NLS-1$

        if (pNewBuilding.isPersisted()) {
            throw new IllegalArgumentException("永続化済みのエンティティは処理できません. " + ECandidateBuilding.class.getSimpleName() + ".id -> '" //$NON-NLS-1$ //$NON-NLS-2$
                    + pNewBuilding.getId() + "'"); //$NON-NLS-1$
        }

        try {
            final EUser loginUser = this.userService.search(pLoginUser.getId());
            pNewBuilding.setRegistrationUser(loginUser);

            getEntityManager().persist(pNewBuilding);

            // TODO pImageDataの処理

        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @see sandbox.quickstart.service.IBuildingService#search(long)
     */
    @Override
    public ECandidateBuilding search(final long pId) throws NotFound {
        return this.findByIdCore(ECandidateBuilding.class, pId);
    }

    /**
     * @see sandbox.quickstart.service.IBuildingService#search(SearchCondition, long, long, jabara.general.Sort)
     */
    @Override
    public List<ECandidateBuilding> search(final SearchCondition pCondition, final long pFirst, final long pCount, final Sort pSort) {
        ArgUtil.checkNull(pCondition, "pCondition"); //$NON-NLS-1$
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$
        if (pFirst > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("pFirst is over Integer.MAX_VALUE. -> " + pFirst); //$NON-NLS-1$
        }
        if (pCount > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("pCount is over Integer.MAX_VALUE. -> " + pCount); //$NON-NLS-1$
        }

        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<ECandidateBuilding> query = builder.createQuery(ECandidateBuilding.class);
        final Root<ECandidateBuilding> root = query.from(ECandidateBuilding.class);

        final List<Predicate> ps = new ArrayList<>();
        addSearchPredicates(ps, pCondition, builder, root);
        query.where(ps.toArray(new Predicate[ps.size()]));

        query.orderBy(convertOrder(pSort, builder, root));

        return em.createQuery(query).setFirstResult((int) pFirst).setMaxResults((int) pCount).getResultList();
    }

    /**
     * @see sandbox.quickstart.service.IBuildingService#search(sandbox.quickstart.service.IBuildingService.SearchCondition, jabara.general.Sort)
     */
    @Override
    public List<ECandidateBuilding> search(final SearchCondition pCondition, final Sort pSort) {
        return this.search(pCondition, 0, Integer.MAX_VALUE, pSort);
    }

    /**
     * @see sandbox.quickstart.service.IBuildingService#update(sandbox.quickstart.model.LoginUser, long, sandbox.quickstart.entity.ECandidateBuilding,
     *      java.io.InputStream)
     */
    @Override
    public void update(final LoginUser pLoginUser, final long pId, final ECandidateBuilding pUpdateValues, final InputStream pImageData) {
        ArgUtil.checkNull(pLoginUser, "pLoginUser"); //$NON-NLS-1$
        ArgUtil.checkNull(pUpdateValues, "pUpdateValues"); //$NON-NLS-1$

        try {
            final ECandidateBuilding inDb = this.search(pId);
            final EUser registrationUser = this.userService.search(pLoginUser.getId());
            inDb.setAddress(pUpdateValues.getAddress());
            inDb.setFreeText(pUpdateValues.getFreeText());
            inDb.setName(pUpdateValues.getName());
            inDb.setPosition(pUpdateValues.getPosition());
            inDb.setRegistrationUser(registrationUser);

            // TODO pImageDataの処理

        } catch (final NotFound e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private static <X> void addLikePredicateIfNotEmpty( //
            final List<Predicate> pPredicates //
            , final SingularAttribute<X, String> pAttribute //
            , final String pLikePlainValue //
            , final CriteriaBuilder pBuilder //
            , final Path<X> pPath //
    ) {
        if (isEmpty(pLikePlainValue)) {
            return;
        }
        final LikeValue lv = LikeValue.get(pLikePlainValue);
        final Predicate like = pBuilder.like(pPath.get(pAttribute), lv.getValue(), lv.getEscapeChar());
        pPredicates.add(like);
    }

    private static void addSearchPredicates( //
            final List<Predicate> pPredicates //
            , final SearchCondition pCondition //
            , final CriteriaBuilder builder //
            , final Root<ECandidateBuilding> root //
    ) {
        addLikePredicateIfNotEmpty(pPredicates, ECandidateBuilding_.address, pCondition.getAddressPart(), builder, root);
        addLikePredicateIfNotEmpty(pPredicates, ECandidateBuilding_.name, pCondition.getNamePart(), builder, root);

        if (!isEmpty(pCondition.getRegistrationUserId())) {
            final Predicate equal = builder.equal( //
                    root.get(ECandidateBuilding_.registrationUser).get(EUser_.userId) //
                    , pCondition.getRegistrationUserId());
            pPredicates.add(equal);
        }
    }

    private static boolean isEmpty(final String s) {
        return s == null || s.trim().length() == 0;
    }

    private static class LikeValue implements Serializable {
        private static final long serialVersionUID = -1838054737628229220L;

        private final char        escapeChar;
        private final String      value;

        LikeValue(final char pEscapeChar, final String pValue) {
            this.escapeChar = pEscapeChar;
            this.value = pValue;
        }

        char getEscapeChar() {
            return this.escapeChar;
        }

        String getValue() {
            return this.value;
        }

        static LikeValue get(final String pPlainText) {
            final char C = '\\'; // TODO これだとpPlainTextが\を含むときに都合が悪い.
            return new LikeValue(C, '%' + pPlainText + '%');
        }

    }
}
