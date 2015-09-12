package hitwh.yxx.wei.web.action;

import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

@Controller
public class SearchAction extends ActionSupport {

	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String search(){
		addActionMessage("  Message of 131110526 ");
		
		
		return SUCCESS;
	}

}
