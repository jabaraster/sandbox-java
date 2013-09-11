/**
 * 
 */
package sandbox.quickstart.service.impl;

import jabara.jpa_guice.ThreadLocalEntityManagerFactoryHandler;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import sandbox.quickstart.Environment;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @param <S> テスト対象のサービスの型.
 * @author jabaraster
 */
public abstract class JpaDaoRule<S> implements TestRule {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager        entityManager;
    private S                    sut;

    /**
     * @see org.junit.rules.TestRule#apply(org.junit.runners.model.Statement, org.junit.runner.Description)
     */
    @Override
    public Statement apply(final Statement pBase, @SuppressWarnings("unused") final Description pDescription) {
        return new Statement() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void evaluate() throws Throwable {
                final EntityManagerFactory original = Persistence.createEntityManagerFactory(Environment.getApplicationName());
                JpaDaoRule.this.entityManagerFactory = ThreadLocalEntityManagerFactoryHandler.wrap(original);
                JpaDaoRule.this.entityManager = JpaDaoRule.this.entityManagerFactory.createEntityManager();
                JpaDaoRule.this.sut = createService(JpaDaoRule.this.entityManagerFactory);

                JpaDaoRule.this.entityManager.getTransaction().begin();
                try {
                    pBase.evaluate();
                } finally {
                    try {
                        JpaDaoRule.this.entityManager.getTransaction().rollback();
                    } catch (final Throwable e) {
                        e.printStackTrace();
                    }
                    JpaDaoRule.this.entityManagerFactory.close();
                }

            }
        };
    }

    /**
     * @return entityManagerを返す.
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * @return sutを返す.
     */
    public S getSut() {
        return this.sut;
    }

    /**
     * @param pEntityManagerFactory -
     * @return -
     */
    protected abstract S createService(EntityManagerFactory pEntityManagerFactory);

}
