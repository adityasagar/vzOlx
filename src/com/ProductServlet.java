package com;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		String flag = request.getParameter("flag")==null?"":(String)request.getParameter("flag");
		System.out.println("inside Prduct Servlet"+flag);

		String type=request.getParameter("type")==null?"":(String)request.getParameter("type");
		String value = request.getParameter("value")==null?"":(String)request.getParameter("value");
		String userId=request.getParameter("userid")==null?"":(String)request.getParameter("userid");
		String name = request.getParameter("name")==null?"":(String)request.getParameter("name");
		request.setAttribute("name",(String)(request.getParameter("name").trim()));
		request.setAttribute("userid",(String)(request.getParameter("userid").trim()));
		try{
			if(type.trim().equalsIgnoreCase("category")){
			ArrayList<ProductVO> results = null;
			try {
				results = ProductUtility.getCategoryResults( value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("type", type);
			request.setAttribute("value", value);
			request.setAttribute("results",results);
			request.getRequestDispatcher("prodDet.jsp").include(request, response);
			}
			if(type.trim().equalsIgnoreCase("search")){
				ArrayList<ProductVO> results= ProductUtility.getSearchResults( value);
				request.setAttribute("type", type);
				request.setAttribute("value", value);
				request.setAttribute("results",results);
				request.getRequestDispatcher("prodDet.jsp").include(request, response);
				
			}
			if(type.trim().equalsIgnoreCase("mostviewed")){
				ArrayList<ProductVO> results= ProductUtility.getMostViewedResults( value);
				request.setAttribute("type", type);
				request.setAttribute("value", value);
				request.setAttribute("results",results);
				request.getRequestDispatcher("prodDet.jsp").include(request, response);
				
			}
			if(type.trim().equalsIgnoreCase("recent")){
				ArrayList<ProductVO> results= ProductUtility.getRecentResults( value);
				request.setAttribute("type", type);
				request.setAttribute("value", value);
				request.setAttribute("results",results);
				request.getRequestDispatcher("prodDet.jsp").include(request, response);
				
			}
			if(type.trim().equalsIgnoreCase("addProj")){
				request.setAttribute("type", type);
				request.setAttribute("value", value);
				request.setAttribute("name",name);
				request.setAttribute("userid",userId);
				request.getRequestDispatcher("addProd.jsp").include(request, response);
				
			}
			if(type.trim().equalsIgnoreCase("newProd")){
				ProductVO newProduct = new ProductVO();
				newProduct.setName(request.getParameter("title")==null?"":(String)request.getParameter("title"));
				newProduct.setPrice(request.getParameter("price")==null?0:Float.valueOf(request.getParameter("price")));
				newProduct.setCategory(request.getParameter("Category")==null?"":(String)request.getParameter("Category"));
				newProduct.setReason(request.getParameter("reason")==null?"":(String)request.getParameter("reason"));
				newProduct.setDescription(request.getParameter("desc")==null?"":(String)request.getParameter("desc"));
				newProduct.setOwnerId(Long.valueOf(userId));
				boolean inserted = false;
				try {
					inserted = ProductUtility.productEntry(newProduct);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("inserted", inserted);
				request.setAttribute("type", type);
				request.setAttribute("value", value);
				//request.setAttribute("name",name);
				//request.setAttribute("userid",userId);
				request.getRequestDispatcher("addProd.jsp").include(request, response);
				
			}
			
			if(flag.trim().equalsIgnoreCase("email")){
				long productid= request.getParameter("productid")==null?0:Long.valueOf(request.getParameter("productid"));
				long userid=request.getParameter("userid")==null?0:Long.valueOf(request.getParameter("userid"));
				String message=request.getParameter("message")==null?"":(String)(request.getParameter("message"));
				ProductVO product = null;
				try {
					product = ProductUtility.getProduct(productid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				UserVO seller= RegisterUtility.getUser(product.getOwnerId());
				UserVO buyer= RegisterUtility.getUser(userid);
				ProductUtility.sendEmail(product, buyer, seller, message);			
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");
		        response.setHeader("Cache-control", "no-cache, no-store");
		        response.setHeader("Pragma", "no-cache");
		        response.setHeader("Expires", "-1");
		        response.setHeader("Access-Control-Allow-Origin", "*");
		        response.setHeader("Access-Control-Allow-Methods", "POST");
		        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		        response.setHeader("Access-Control-Max-Age", "86400");
		        out.println("Mail Successfully sent to the Owner!");
		        out.close();
				//request.getRequestDispatcher("addProd.jsp").include(request, response);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
}
