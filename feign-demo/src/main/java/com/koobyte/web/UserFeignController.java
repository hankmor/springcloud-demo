package com.koobyte.web;

import com.koobyte.domain.User;
import com.koobyte.domain.UserQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by sun on 2021/7/22.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
@RestController
@RequestMapping("/user")
public class UserFeignController {
	//~ Static fields/constants/initializer


	//~ Instance fields

	private static final Map<Long, User> users = new ConcurrentHashMap<>();
	private static final AtomicLong id = new AtomicLong();

	static {
		// 初始化数据
		users.put(id.incrementAndGet(), new User(id.get(), "koobyte", 10));
		users.put(id.incrementAndGet(), new User(id.get(), "张三", 90));
		users.put(id.incrementAndGet(), new User(id.get(), "李四", 30));
		users.put(id.incrementAndGet(), new User(id.get(), "王五", 38));
	}

	//~ Constructors


	//~ Methods

	@GetMapping(value = "/{id}")
	public User queryUser(@PathVariable Long id) {
		return users.get(id);
	}

	@GetMapping
	public List<User> queryAllUser() {
		return new ArrayList<>(users.values());
	}

	// 按照条件查询
	@GetMapping("/query")
	public List<User> query(UserQuery query) {
		return this.queryByJson(query);
	}

	// 按照条件查询，传递json格式参数
	@GetMapping("/query/json")
	public List<User> queryByJson(@RequestBody UserQuery query) {
		String name = query.getName();
		Integer maxAge = query.getMaxAge();
		Integer minAge = query.getMinAge();
		List<User> users = this.queryAllUser();
		Predicate<User> predicate = user -> true;
		if (StringUtils.hasLength(name)) {
			predicate = predicate.and(user -> user.getName().contains(name));
		}
		if (minAge != null) {
			predicate = predicate.and(user -> user.getAge() != null && user.getAge() >= minAge);
		}
		if (maxAge != null) {
			predicate = predicate.and(user -> user.getAge() != null && user.getAge() <= maxAge);
		}
		return users.stream().filter(predicate).collect(Collectors.toList());
	}

	// 故意让添加时按照json请求
	@PostMapping
	public Long add(@RequestBody User user) {
		assert user != null;
		Long cid = id.incrementAndGet();
		user.setId(cid);
		users.put(cid, user);
		return cid;
	}

	// 故意让修改时按照表单请求
	@PutMapping
	public void update(User user) {
		assert user != null;
		assert user.getId() != null;
		User srcUser = this.queryUser(user.getId());
		if (user.getName() != null)
			srcUser.setName(user.getName());
		if (user.getAge() != null)
			srcUser.setAge(user.getAge());
		users.put(srcUser.getId(), srcUser);
	}

	@DeleteMapping("/{id}")
	public Long delete(@PathVariable Long id) {
		User user = users.remove(id);
		return user.getId();
	}
}