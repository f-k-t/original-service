package controllers.accounts;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Account;
import models.validators.AccountValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class AccountsCreateServlet
 */
@WebServlet("/accounts/create")
public class AccountsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountsCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String _token = (String) request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Account a = new Account();

            a.setCode(request.getParameter("code"));
            a.setName(request.getParameter("name"));
            a.setPassword(
                    EncryptUtil.getPasswordEncrypt(
                            request.getParameter("password"),
                            (String) this.getServletContext().getAttribute("pepper")));
            //a.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            a.setCreated_at(currentTime);
            a.setUpdated_at(currentTime);
            a.setDelete_flag(0);

            List<String> errors = AccountValidator.validate(a, true, true, true);
            if (errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("account", a);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/accounts/new.jsp");
                rd.forward(request, response);
            } else {
                em.getTransaction().begin();
                em.persist(a);
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "登録が完了しました。");

                response.sendRedirect(request.getContextPath() + "/accounts/index");
            }
        }
    }

}
