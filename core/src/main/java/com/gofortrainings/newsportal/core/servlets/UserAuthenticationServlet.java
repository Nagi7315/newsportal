package com.gofortrainings.newsportal.core.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gofortrainings.newsportal.core.models.UserInfo;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/newsportal/user-service")
public class UserAuthenticationServlet extends SlingAllMethodsServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(UserAuthenticationServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try (ResourceResolver resolver = request.getResourceResolver()) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            JsonObjectBuilder jsonObj = Json.createObjectBuilder();
            String userID = resolver.getUserID();
            if (userID.equals("anonymous")) {

                jsonObj.add("isLoggedIn", false);
                out.print(jsonObj.build().toString());
            } else {

                UserManager userManager = resolver.adaptTo(UserManager.class);
                User user = (User) userManager.getAuthorizable(userID);
                UserInfo userInfo = new UserInfo();
                userInfo.setLastName(user.getProperty("./profile/familyName") != null ? user.getProperty("./profile/familyName")[0].getString() : null);
                userInfo.setFirstName(user.getProperty("./profile/givenName") != null ? user.getProperty("./profile/givenName")[0].getString() : null);
                userInfo.setEmail(user.getProperty("./profile/email") != null ? user.getProperty("./profile/email")[0].getString() : null);
                ObjectMapper objectMapper = new ObjectMapper();
                String userJson = objectMapper.writeValueAsString(userInfo);
                jsonObj.add("isLoggedIn", true);
                jsonObj.add("userInfo", userJson);
                out.print(jsonObj.build().toString());
            }
        } catch (RepositoryException e) {
            log.error("Error : While getting ResourceResolver");
        }
    }
    // {isLoggedIn:true,userInfo:{firstName:'',lastName:'',email}}
    // {isLoggedIn:false,userInfo:{firstName:'',lastName:'',email}}
}
