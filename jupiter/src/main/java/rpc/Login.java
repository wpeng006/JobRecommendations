package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import db.MySQLConnection;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		JSONObject output = new JSONObject();
		if (session != null) {
			MySQLConnection connection = new MySQLConnection();
			String userId = session.getAttribute("user_id").toString();
			output.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
			connection.close();
		} else {
			output.put("status", "Invalid Session");
			response.setStatus(403);
		}
		RpcHelper.writeJsonObject(response, output);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get login info from request body
		JSONObject input = new JSONObject(IOUtils.toString(request.getReader()));
		String userId = input.getString("user_id");
		String password = input.getString("password");
		
		//connect to db 
		MySQLConnection conn = new MySQLConnection();
		
		//verify user in db
		//Create session if verify true
		JSONObject output = new JSONObject();
		if (conn.verifyLogin(userId, password)) {
			HttpSession session = request.getSession();
			session.setAttribute("user_id", userId);
			// set session to expire in 15 mins
			session.setMaxInactiveInterval(15 * 60);
			output.put("status", "OK").put("user_id", userId).put("name", conn.getFullname(userId));
		} else {
			output.put("status", "User Doesn't Exist");
			response.setStatus(401);
		}
		conn.close();
		RpcHelper.writeJsonObject(response, output);
	}

}
