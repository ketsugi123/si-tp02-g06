package businessLogic.BLserviceUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    public TransactionManager(EntityManager em){
        this.em = em;
    }

    private EntityManager em;
    public EntityTransaction startTransaction(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        return transaction;
    }

    public void setIsolationLevel(Connection cn, Integer isolationLevel, EntityTransaction transaction) throws SQLException {
        transaction.rollback();
        cn.setTransactionIsolation(isolationLevel);
        transaction.begin();
    }
}
