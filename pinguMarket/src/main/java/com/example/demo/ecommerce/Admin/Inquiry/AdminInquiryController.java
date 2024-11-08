package com.example.demo.ecommerce.Admin.Inquiry;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.ecommerce.Admin.AdminService;
import com.example.demo.ecommerce.Admin.Notice.AdminNoticeForm;
import com.example.demo.ecommerce.CsAnswer.CsAnswerForm;
import com.example.demo.ecommerce.CsAnswer.CsAnswerRepository;
import com.example.demo.ecommerce.CsAnswer.CsAnswerService;
import com.example.demo.ecommerce.CsQuestion.CsQuestionForm;
import com.example.demo.ecommerce.CsQuestion.CsQuestionRepository;
import com.example.demo.ecommerce.CsQuestion.CsQuestionService;
import com.example.demo.ecommerce.CsQuestion.UserException;
import com.example.demo.ecommerce.Entity.Admin;
import com.example.demo.ecommerce.Entity.CsAnswer;
import com.example.demo.ecommerce.Entity.CsQuestion;
import com.example.demo.ecommerce.Entity.Notice;
import com.example.demo.ecommerce.Review.CanNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어 줌
@Controller
@RequestMapping("/admin")
public class AdminInquiryController {
	
	private final CsQuestionService cqs;
	private final CsAnswerService cas;
	private final AdminService as;
	private final CsQuestionRepository cqr;
	private final CsAnswerRepository car;
	
	//---------------관리자페이지 > 문의 관리(리스트)----------------------------------
//	@PreAuthorize("isAuthenticated()") // 로그인 한 경우에만 요청 처리
	@GetMapping("/cs") 
	public String Inquiry(Model model) {
        List<CsQuestion> Q = this.cqr.findAll();
        model.addAttribute("Q", Q);
                          //" "안에 있는 값이 html에서 인식할 텍스트
        return "/Admin/AdminInquiry";  
	}
	
	//---------------관리자페이지 > 문의 관리(다중 선택 삭제)----------------------------
//	@PreAuthorize("isAuthenticated()") // 로그인 한 경우에만 요청 처리
	@PostMapping("/cs/delete")
	@ResponseBody
	public Map<String, Object> AdminInquiryDelete(@RequestBody Map<String, List<String>> payload) {
	    Map<String, Object> response = new HashMap<>();
	    List<String> ids = payload.get("ids"); 							//ids = id값들을 저장한 리스트
	    System.out.println("Received IDs: " + ids);
	try {
	        for (String csQuestionIdStr : ids) {
	            int csQuestionId = Integer.parseInt(csQuestionIdStr); 	//저장한 json타입의 id값을 Integer타입으로 변환해 csQuestionId에 할당
	            System.out.println("id확인: " + csQuestionId);
	            CsQuestion cq = this.cqs.getQuestion(csQuestionId); 	//csQuestionId로 리뷰 데이터를 받아옴
	            this.cqs.delete(cq);									//받아온 문의글(질문) 데이터 삭제 -> 답변도 같이 삭제되는지?
	        }
	        response.put("success", true);								//성공적으로 삭제
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", false);								//삭제 실패 시 false 값을 넘김(실패했습니다 알림창)
	    }
	    return response;
    }	
	
	
	//---------------관리자페이지 > 문의 관리 > 상세페이지-------------------------------
//	@PreAuthorize("isAuthenticated()") // 로그인 한 경우에만 요청 처리
	@GetMapping("/cs/{csQuestionId}") 
    public String AdminInquiry(Model model, @PathVariable("csQuestionId") Integer csQuestionId, CsAnswerForm csAnswerForm) 
    		throws UserException{
		//csQuestionId로 조회해서 가져오기
		CsQuestion Q = this.cqs.getQuestion(csQuestionId);
        model.addAttribute("Q", Q);
        return "/Admin/AdminInquiry_detail";
    }

	//---------------관리자페이지 > 문의 관리 > 상세페이지 > 답변 신규등록-------------------
//	@PreAuthorize("isAuthenticated()") // 로그인 한 경우에만 요청 처리
//	@PostMapping("/cs/newAnswer/{id}")
//	public String createAnswer(Model model, @PathVariable("csQuestionId") Integer csQuestionId,
//			@RequestParam(value="content") String content,
//			@Valid CsAnswerForm answerForm, BindingResult bindingResult,
//			Principal principal) {
//		try {
//			CsQuestion q1 = this.cqs.getQuestion(csQuestionId);
//			Admin admin  = this.cas.getAdmin.get();
//			if(bindingResult.hasErrors()) {
//				model.addAttribute("question", q1);
//				return "/Admin/AdminInquiry";
//			}
//			this.as.returnCreate(answerForm,answerForm.getContents(), admin);
//			//create(q1, answerForm.getContent(), admin);
//		} catch (CanNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		return String.format("redirect:/admin/cs/{csQuestionId}", csQuestionId);
//	}
	
	
	//---------------관리자페이지 > 문의 관리 > 상세페이지 > 답변 수정하기-------------------
//	@PreAuthorize("isAuthenticated()") // 로그인 한 경우에만 요청 처리
//	@PostMapping("/cs/updateAnswer") 
//    public String updateCsAnswer(@Valid CsAnswerForm csAnswerForm, 
//    		@RequestParam(value="noticeId") Integer noticeId) throws CanNotFoundException {
//		Admin admin = this.as.getAdmin(1);
////		로그인 기능 구현 전이라 임의로 1을 넘김
//		CsAnswer ca = this.cas.getCsAnswer(csAnswerId);
////		수정한 데이터 저장하는 메소드 호출
//		this.cas.update(ca, csAnswerForm.getTitle(), csAnswerForm.getContents());
//		
//		cas.update(ca);
//        return "redirect:/admin/cs";
//    }
	

	
}
