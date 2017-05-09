package com.tvd12.ezyfoxserver.morphia.testing.data;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Entity(value = "ezyfox.mongodb.testing.kitty", noClassnameStored = true)
public class Kitty {
	@Id
	private Long id = (long) ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
	private String name = "cat#" + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
	private String fixedValue = "fixedValue";
	private Set<String> set = new HashSet<>();
	public int age = 10;
	
	public Kitty(Long id) {
		this.id = id;
	}
}
