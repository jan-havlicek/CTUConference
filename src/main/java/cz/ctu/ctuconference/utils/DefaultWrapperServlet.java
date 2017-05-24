package cz.ctu.ctuconference.utils;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Nick Nemame on 05.10.2016.
 */
@WebServlet("/assets/*")
public class DefaultWrapperServlet extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		RequestDispatcher rd = getServletContext().getNamedDispatcher("default");

		HttpServletRequest wrapped = new HttpServletRequestWrapper(req) {
			public String getServletPath() { return "/assets"; }
		};

		rd.forward(wrapped, resp);
	}
}
