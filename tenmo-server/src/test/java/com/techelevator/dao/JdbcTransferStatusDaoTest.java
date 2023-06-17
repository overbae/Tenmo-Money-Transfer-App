package com.techelevator.dao;
import static org.junit.Assert.assertNull;

import com.techelevator.tenmo.dao.JdbcTransferStatusDao;
import org.junit.Test;

public class JdbcTransferStatusDaoTest {
    /**
     * Method under test: {@link JdbcTransferStatusDao#getTransferStatusById(int)}
     */
    @Test
    public void testGetTransferStatusById() {
        assertNull((new JdbcTransferStatusDao()).getTransferStatusById(1));
    }

    /**
     * Method under test: {@link JdbcTransferStatusDao#getTransferStatusByDesc(String)}
     */
    @Test
    public void testGetTransferStatusByDesc() {
        assertNull((new JdbcTransferStatusDao()).getTransferStatusByDesc("The characteristics of someone or something"));
    }
}