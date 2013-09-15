package sandbox.quickstart.service.impl;

import jabara.general.ArgUtil;
import jabara.general.NotFound;
import jabara.general.Sort;
import jabara.jpa.JpaDaoBase;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import sandbox.quickstart.entity.ELoginPassword;
import sandbox.quickstart.entity.EUser;
import sandbox.quickstart.service.IUserService;

/**
 * 
 */
public class UserServiceImpl extends JpaDaoBase implements IUserService {
    private static final long serialVersionUID = 5771084556720067384L;

    /**
     * @param pEntityManagerFactory DBアクセス用オブジェクト.
     */
    @Inject
    public UserServiceImpl(final EntityManagerFactory pEntityManagerFactory) {
        super(pEntityManagerFactory);
    }

    /**
     * @see sandbox.quickstart.service.IUserService#search(long)
     */
    @Override
    public EUser search(final long pId) throws NotFound {
        return this.findByIdCore(EUser.class, pId);
    }

    /**
     * @see sandbox.quickstart.service.IUserService#getAll(jabara.general.Sort)
     */
    @Override
    public List<EUser> getAll(final Sort pSort) {
        ArgUtil.checkNull(pSort, "pSort"); //$NON-NLS-1$
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<EUser> query = builder.createQuery(EUser.class);
        final Root<EUser> root = query.from(EUser.class);
        query.orderBy(convertOrder(pSort, builder, root));
        return em.createQuery(query).getResultList();
    }

    /**
     * @see sandbox.quickstart.service.IUserService#insertAdministratorIfNotExists()
     */
    @Override
    public void insertAdministratorIfNotExists() {
        if (existsAministrator()) {
            return;
        }

        final EntityManager em = getEntityManager();

        final EUser member = new EUser();
        member.setAdministrator(true);
        member.setUserId(EUser.DEFAULT_ADMINISTRATOR_USER_ID);
        em.persist(member);

        final ELoginPassword password = new ELoginPassword();
        password.setPassword(EUser.DEFAULT_ADMINISTRATOR_PASSWORD);
        password.setUser(member);
        em.persist(password);
    }

    private boolean existsAministrator() {
        final EntityManager em = getEntityManager();
        final CriteriaBuilder builder = em.getCriteriaBuilder();
        final CriteriaQuery<String> query = builder.createQuery(String.class);
        query.from(EUser.class);

        final String DUMMY = "X"; //$NON-NLS-1$
        query.select(builder.literal(DUMMY).alias(DUMMY));

        return !em.createQuery(query).setMaxResults(1).getResultList().isEmpty();
    }

}
