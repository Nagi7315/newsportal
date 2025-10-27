package com.gofortrainings.newsportal.core.servlets;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.taskmanagement.TaskManagerException;
import com.adobe.granite.taskmanagement.TaskManagerFactory;
import com.adobe.granite.workflow.exec.InboxItem;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.mail.MessagingException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(
        service = Servlet.class,
        property = {
                "sling.servlet.methods=POST",
                "sling.servlet.paths=/bin/tasknotification"
        }
)
public class TaskNotificationServlet extends SlingAllMethodsServlet {

    @Reference
    private MessageGatewayService messageGatewayService;

    private static final String USER_ID = "admin"; // Change this to the target user or group

    private static final String EMAIL_TEMPLATE = "/apps/newsportal/email/html5-template.txt";

    private static final Logger log = LoggerFactory.getLogger(TaskNotificationServlet.class);

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String payloadPath = request.getParameter("payload");

        ResourceResolver resolver = request.getResourceResolver();
        createInboxTask(resolver, title, description, payloadPath);
        sendEmailNotification(resolver, payloadPath);
        response.getWriter().write("Task Created Successfully!");
    }
    public void createInboxTask(ResourceResolver resolver, String title, String description, String payloadPath) {
        try {
            TaskManager taskManager = resolver.adaptTo(TaskManager.class);
            if(taskManager != null){

                TaskManagerFactory taskManagerFactory = taskManager.getTaskManagerFactory();

                Task newTask = taskManagerFactory.newTask(null);
                newTask.setName(title);
                newTask.setContentPath(payloadPath);
                newTask.setPriority(InboxItem.Priority.HIGH); // Optionally set priority (High, Medium, Low)
                newTask.setDescription(description);
                newTask.setInstructions(description);
                newTask.setCurrentAssignee(USER_ID);
                taskManager.createTask(newTask);
            }

        } catch (TaskManagerException e) {
            e.printStackTrace();
        }

    }

    private void sendEmailNotification(ResourceResolver resolver, String assetPath) {
        try {
            Node templateNode = resolver.getResource(EMAIL_TEMPLATE).adaptTo(Node.class);
            final Map<String, String> parameters = new HashMap<>();
            parameters.put("title", "Demo Email");
            parameters.put("name", "arunpatidar02");
            parameters.put("id", "7315");
            final MailTemplate mailTemplate = MailTemplate.create(EMAIL_TEMPLATE, templateNode.getSession());
            HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(parameters), HtmlEmail.class);
            MessageGateway<HtmlEmail> gateway = messageGatewayService.getGateway(HtmlEmail.class);
            email.setSubject("AEM - Demo Email for Templated email");
            email.addTo("jagirinagireddy@gmail.com");
            if (gateway != null) {
                gateway.send(email);
                log.info("Email sent for asset {}", assetPath);
            } else {
                log.warn("No MessageGateway available for HtmlEmail");
            }

        } catch (EmailException | RepositoryException e) {
            log.error("Failed to send email notification for asset {}", assetPath, e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


