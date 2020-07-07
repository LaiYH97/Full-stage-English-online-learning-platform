package com.online.college.ocSpringBoot;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiresPermissions("activity")
public class ActivityController {

	@RequestMapping("/activity")
	public String activity() {
		return "activity/activityList";
	}
}
