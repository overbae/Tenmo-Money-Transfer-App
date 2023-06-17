package com.techelevator.dao;

import static org.junit.Assert.assertNull;

import com.techelevator.tenmo.dao.JdbcTransferTypeDao;
import org.junit.Test;

public class JdbcTransferTypeDaoTest {
    /**
     * Method under test: {@link JdbcTransferTypeDao#getTransferTypeById(int)}
     */
    @Test
    public void testGetTransferTypeById() {
        assertNull((new JdbcTransferTypeDao()).getTransferTypeById(1));
    }

    /**
     * Method under test: {@link JdbcTransferTypeDao#getTransferTypeByDesc(String)}
     */
    @Test
    public void testGetTransferTypeByDesc() {
        assertNull((new JdbcTransferTypeDao()).getTransferTypeByDesc(" "));
    }
}

