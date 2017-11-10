package com.tvd12.ezyfoxserver.morphia.testing.data;

import java.util.concurrent.ThreadLocalRandom;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity(value = "ezyfox.mongodb.testing.chicken", noClassnameStored = true)
public class Chickend {
	@Id
	private Long id = (long) ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
	private String name = "cat#" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
	
	public Chickend(Long id) {
		this.id = id;
	}
}
