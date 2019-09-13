package com.nsoroma.trackermonitoring.scheduler.service;

import com.nsoroma.trackermonitoring.model.trackerstate.TrackerState;
import com.nsoroma.trackermonitoring.services.DocumentsService;
import com.nsoroma.trackermonitoring.services.Trackers;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SheetDataWriter;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public class Email extends QuartzJobBean {

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Trackers trackerService;

    @Autowired
    private DocumentsService documentsService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("executing schedule");
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String receiverMail = jobDataMap.getString("email");
        Optional<String> lastUpdate = Optional.ofNullable(jobDataMap.getString("lastUpdateDate"));
        Optional<String> trackerType = Optional.ofNullable(jobDataMap.getString("trackerType"));
        Optional<String> customerId = Optional.ofNullable(jobDataMap.getString("customerId"));
        Optional<String> order = Optional.ofNullable(null);

        LinkedHashSet<TrackerState> trackerStates;

        try {
            trackerStates =  trackerService.getTrackers(lastUpdate,customerId,trackerType, order);
            Sheet trackerStateSheet = documentsService.generateExcellSheet(trackerStates);
            FileOutputStream fos = new FileOutputStream("tracker.xls");
            trackerStateSheet.getWorkbook().write(fos);
            fos.close();
            FileDataSource source = new FileDataSource("tracker.xls");
            sendMail(mailProperties.getUsername(), receiverMail, subject, body, source);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendMail(String senderMail, String receiverMail, String subject, String body, FileDataSource trackerStateSheet) {

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(senderMail);
            messageHelper.setTo(receiverMail);
            messageHelper.addAttachment("Tracker States.xlsx", trackerStateSheet);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
