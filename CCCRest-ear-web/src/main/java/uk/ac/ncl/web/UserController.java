package uk.ac.ncl.web;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.ncl.core.CCCEngine;
import uk.ac.ncl.event.Event;
import uk.ac.ncl.event.EventStatus;
import uk.ac.ncl.event.Operation;
import uk.ac.ncl.event.Operation.OperationName;
import uk.ac.ncl.rop.Obligation;
import uk.ac.ncl.rop.Prohibition;
import uk.ac.ncl.rop.Right;
import uk.ac.ncl.user.User;
import uk.ac.ncl.userBo.UserBo;

@Controller
public class UserController {
    @Autowired
    private UserBo ub;

    @Autowired
    private CCCEngine engine;

    @RequestMapping("")
    public String index() {
        return "index";
    }

    @RequestMapping("/index")
    public String homepage() {
        return "index";
    }

    @RequestMapping("/signup")
    public String signup() {
        return "signup";
    }

    @RequestMapping("/signout")
    public String signout(HttpServletRequest req) throws IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        return "index";
    }

    @RequestMapping("/ropInfo")
    public String ropInfo(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "loginError";
        } else {
            String userName = ((User) session.getAttribute("user")).getName();
            String password = ((User) session.getAttribute("user")).getPassword();
            try {
                User user = ub.get(userName, password);
                Set<Right> userRight = user.getRightSet();
                Set<Obligation> userObligation = user.getObligationSet();
                Set<Prohibition> userProhibition = user.getProhibitionSet();
                model.addAttribute("userRight", userRight);
                model.addAttribute("userObligation", userObligation);
                model.addAttribute("userProhibition", userProhibition);
                return "ropInfo";
            } catch (Exception e) {
                return "fail";
            }
        }
    }

    @RequestMapping("/signin")
    public String signin(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return "signin";
        } else {
            return "index";
        }
    }

    @Transactional
    @RequestMapping(value = {"/signup"}, method = {RequestMethod.POST}, params = {
            "userName", "password", "role"})
    public String signup(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        try {
            User user = ub.create(userName, password, role);
            Operation operation = new Operation(OperationName.register, null, null);
            Event event = new Event(userName, operation, EventStatus.succeed);
            engine.run(event);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = {"/signin"}, method = {RequestMethod.POST}, params = {
            "userName", "password"})
    public String signin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        try {
            User user = ub.get(userName, password);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                return "success";
            } else {
                return "loginError";
            }
        } catch (Exception e) {
            return "fail";
        }
    }

}
