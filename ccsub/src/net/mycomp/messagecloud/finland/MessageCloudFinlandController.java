package net.mycomp.messagecloud.finland;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("mcgfl")
public class MessageCloudFinlandController {

	@RequestMapping("lp")
	public ModelAndView lp(ModelAndView modelAndView) {
		modelAndView.setViewName("mcgfinland/lp");
		return modelAndView;
	}
	@RequestMapping("tc")
	public ModelAndView tc(ModelAndView modelAndView) {
		modelAndView.setViewName("mcgfinland/tc");
		return modelAndView;
	}
	
}
