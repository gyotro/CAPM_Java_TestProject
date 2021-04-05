package com.sap.cap.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;

 @Component
 @ServiceName("AdminService") // perché adminService si chiama il servizio definito in admin-service.cds
public class customHandler implements EventHandler {

    private Map<Object, Map<String, Object>> bolle = new HashMap<>();

     @On(event = CdsService.EVENT_CREATE, entity = "AdminService.Bolle")
     public void onCreate(CdsCreateEventContext context) {
         context.getCqn().entries().forEach(e -> bolle.put(e.get("Bolla"), e));
         context.setResult(context.getCqn().entries());
     }
    @On(event = CdsService.EVENT_READ, entity = "AdminService.Bolle")
     public void onRead(CdsReadEventContext context) {
         context.setResult(bolle.values());
     }
}
