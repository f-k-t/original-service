package controllers.accounts;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Account;

@WebServlet("/accounts/new")
public class AccountsNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AccountsNewServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("account", new Account());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/accounts/new.jsp");
        rd.forward(request, response);
    }

}
