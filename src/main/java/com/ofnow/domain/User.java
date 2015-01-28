package com.ofnow.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author futeshi
 * TODO 将来的にユーザのstatusを持てるようにしたい(active, awayみたいな感じ)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(nullable = false)
	private String uuid;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String mail;
	private boolean inOffice;
	private Date createTime;
	private Date updateTime;
}
