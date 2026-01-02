package vn.van.spring_ai_service.service;

import org.springframework.web.multipart.MultipartFile;
import vn.van.spring_ai_service.dto.request.ChatRequest;
import vn.van.spring_ai_service.dto.response.BillItem;
import vn.van.spring_ai_service.dto.response.ExpenseInfo;
import vn.van.spring_ai_service.dto.response.FilmInfo;

import java.util.List;

public interface ChatService {
    String chatMessage(ChatRequest request);
    List<FilmInfo> chatFilm(ChatRequest request);
    ExpenseInfo chatExpense(ChatRequest request);
    String chatWithImage(MultipartFile file, String message);
    List<BillItem> chatWithBillImage(MultipartFile file, String message);
}
