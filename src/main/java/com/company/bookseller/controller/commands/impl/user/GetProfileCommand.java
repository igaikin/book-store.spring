package com.company.bookseller.controller.commands.impl.user;

import com.company.bookseller.controller.commands.Command;
import com.company.bookseller.service.UserService;
import com.company.bookseller.service.dto.UserDto;
import com.company.bookseller.util.MessageManager;
import jakarta.servlet.http.HttpServletRequest;

public class GetProfileCommand implements Command {
    private final UserService userService;

    public GetProfileCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req) {
        String id = req.getParameter("id");
        UserDto user = userService.get(Long.valueOf(id));
        if (user == null) {
            req.setAttribute("message", MessageManager.getMessage("msg.userNotFound"));
            return "jsp/error.jsp";
        }
        req.setAttribute("user", user);
        return "jsp/profile.jsp";
    }
}
