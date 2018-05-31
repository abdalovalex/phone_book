package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter(filterName = "ContactFilter", urlPatterns = {"/contact-add"})
public class ContactFilter implements Filter
{
    // Длина телефонного номера, учитываются только циры
    private static final int PHONE_LENGTH = 11;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        if (httpRequest.getMethod().equalsIgnoreCase("POST"))
        {
            Boolean isError = false;
            Map<Integer, String> phone_errors = new HashMap<>();
            String[] phone = servletRequest.getParameterValues("Phone[]");
            String name = servletRequest.getParameter("name");
            String value;

            // Проверка обязательного заполнения ФИО контакта
            if (name == null || name.trim().length() == 0)
            {
                httpRequest.getSession().setAttribute("name_error", "Обязательное поле для заполнения");
                isError = true;
            }

            // Проверка телефонных номеров
            for (int i = 0; i < phone.length; i++)
            {
                value = phone[i].replaceAll("[^0-9]", "");
                if (value.length() < PHONE_LENGTH || value.length() > PHONE_LENGTH)
                {
                    phone_errors.put(i, "Не корректный телефон");
                    isError = true;
                }
            }

            // Если есть ошибки валидации, то дальше запрос не пропускаем
            if (isError)
            {
                httpRequest.getSession().setAttribute("phone", phone);
                httpRequest.getSession().setAttribute("phone_errors", phone_errors);
                httpRequest.getSession().setAttribute("socialLink", httpRequest.getParameterValues("SocialLink[]"));

                httpRequest.getRequestDispatcher("/WEB-INF/view/contactAdd.jsp").forward(httpRequest, servletResponse);
                return;
            }
        }

        filterChain.doFilter(httpRequest, servletResponse);
    }

    @Override
    public void destroy()
    {

    }
}
