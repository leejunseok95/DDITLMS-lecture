package com.example.dditlms;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;


@SpringBootTest
class DditlmsApplicationTests {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void contextLoads() {
        try(Connection con = sqlSessionFactory.openSession().getConnection()){
            System.out.println("커넥션 성공");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
