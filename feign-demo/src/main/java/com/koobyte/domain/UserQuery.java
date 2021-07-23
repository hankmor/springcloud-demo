package com.koobyte.domain;

/**
 * Created by sun on 2021/7/23.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class UserQuery {
	//~ Static fields/constants/initializer


	//~ Instance fields

	private String name;
	private Integer minAge;
	private Integer maxAge;

	//~ Constructors


	//~ Methods


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}
}