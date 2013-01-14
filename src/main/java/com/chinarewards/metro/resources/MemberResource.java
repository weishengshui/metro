package com.chinarewards.metro.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.chinarewards.metro.models.Member;

@Path("/member")
public class MemberResource {

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
}
