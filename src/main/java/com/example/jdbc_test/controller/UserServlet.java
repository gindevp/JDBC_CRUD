package com.example.jdbc_test.controller;

import com.example.jdbc_test.dao.UserDAO;
import com.example.jdbc_test.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="UserServlet", value = "/users")
public class UserServlet extends HttpServlet {
private static final long serialVersionUID=1L;
private UserDAO userDAO;
    @Override
    public void init() throws ServletException {
        userDAO= new UserDAO();
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if(action==null){
            action="";
        }
        try{
            switch (action){
                case "create":
                    insertUser(req,resp);
                    break;
                case "edit":
                    updateUser(req,resp);
                    break;
                case "search":
                    searchUserByCountry(req,resp);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void searchUserByCountry(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search= req.getParameter("searchByCountry");

        List<User> user=new UserDAO().selectUserByCountry(search);
        req.setAttribute("user",user);
        RequestDispatcher dispatcher= req.getRequestDispatcher("user/viewSearch.jsp");
        dispatcher.forward(req,resp);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        User book = new User(id, name, email, country);
        userDAO.updateUser(book);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");
        User newUser = new User(name, email, country);
        userDAO.insertUser(newUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action= req.getParameter("action");
        if(action==null){
            action="";
        }
        try{
            switch (action){
                case "create":
                    showNewForm(req,resp);
                    break;
                case "edit":
                    showEditForm(req,resp);
                    break;
                case "delete":
                    deleteUser(req,resp);
                    break;
                case "sort":
                    sortName(req,resp);
                    break;
                default:
                    listUser(req,resp);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sortName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> userList= userDAO.sortName();
        req.setAttribute("listUser",userList);
        RequestDispatcher dispatcher= req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req,resp);
    }

    private void listUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> userList= userDAO.selectAllUsers();
        req.setAttribute("listUser",userList);
        RequestDispatcher dispatcher= req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req,resp);
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);

        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(request, response);

    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.selectUser(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/edit.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(request, response);
    }


}
