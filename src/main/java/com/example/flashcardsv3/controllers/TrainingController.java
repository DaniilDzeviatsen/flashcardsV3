package com.example.flashcardsv3.controllers;



import com.example.flashcardsv3.models.Card;
import com.example.flashcardsv3.service.CardService;
import com.example.flashcardsv3.service.ChapterService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/training")
public class TrainingController extends HttpServlet {

    private CardService cardService;
    private ChapterService chapterService;

    @Override
    public void init()  {
        ServletContext context = getServletContext();
        cardService = (CardService) context.getAttribute("cardService");
        chapterService=(ChapterService) context.getAttribute("chapterService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long chapterId=Long.parseLong(request.getParameter("chapterId"));
        List<Card> cards=cardService.showAllCards(chapterId);
        String responseBody=cards.isEmpty()?"No one card exists":cards.stream()
                .map(card->"%3s %s %s %s".formatted(
                        card.getId(),
                        card.getQuestion(),
                        card.getAnswer(),
                        card.isIs_remembered()
                ))
                .collect(Collectors.joining("\n"));
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(responseBody);
    }
}
