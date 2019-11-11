package com.thanhld.server959.model;

import org.springframework.data.annotation.Id;

public abstract class AbstractModel {
	@Id
	protected String id;
}
