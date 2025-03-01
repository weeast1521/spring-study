package jpabook.jpashop.web;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
    // 회원 가입을 할 때 입력할 정보들은 이름, 도시, 거리, 우편번호임
    // 그냥 Member 에서는 id, 이름, address, orders 등등 받는게 많음
    // form 객체를 만들어서 화면 계층과 서비스 계층을 분리해야한다.

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
