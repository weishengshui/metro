package com.chinarewards.metro.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.models.Member;
import com.chinarewards.metro.models.RegisterResp;
import com.chinarewards.metro.service.member.IMemberService;
@Component
@Path("/member")
public class MemberResource {

	@Autowired
	private IMemberService memberService;
	
	@GET
	@Path("/list")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public List<Member> account() {

		List<Member> members = new ArrayList<Member>();

		Member member = new Member();

		member.setUserName("Kevin");
		member.setAddress("SZ");
		member.setAge(15);
		member.setGender("男");

		Member member2 = new Member();

		member2.setUserName("Helen");
		member2.setAddress("GZ");
		member2.setAge(22);
		member2.setGender("女");

		members.add(member);
		members.add(member2);

		return members;
	}
	
	@POST
	@Path("/save")
	@Produces({ "application/xml", "application/json" })
	public RegisterResp saveMemeber(RegisterResp reg) {

		RegisterResp r =  new RegisterResp();
		Date now = DateTools.dateToHour();
		com.chinarewards.metro.domain.member.Member m =
				new com.chinarewards.metro.domain.member.Member();
		
		com.chinarewards.metro.domain.member.Member mb = memberService.findMemberByPhone(reg.getPhone());
		if(mb == null){
			m.setPhone(reg.getPhone());
			m.setSource(reg.getPosId());
			m.setCreateDate(now);
			memberService.saveT(m);
			r.setResult("0");
		}else{
			r.setResult("1");
		}
		r.setXactTime(now);
		return r;
	}
}
