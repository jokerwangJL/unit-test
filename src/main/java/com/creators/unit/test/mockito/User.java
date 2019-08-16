package com.creators.unit.test.mockito;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ===========================
 * author:       wangjialong
 * date:         2019/8/15
 * time:         11:14
 * description:  请输入描述
 * ============================
 */
@Entity
@Data
@NoArgsConstructor
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false,length = 50)
    private String username;

    private String password;

    @CreationTimestamp
    private Date createDate;

    public User(Long id,String username) {
        this.id = id;
        this.username = username;
    }
}
