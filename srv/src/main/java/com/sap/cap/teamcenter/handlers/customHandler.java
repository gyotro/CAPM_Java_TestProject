package com.sap.cap.teamcenter.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import cds.gen.adminservice.ToTBolleItem;
import cds.gen.adminservice.ToTBolle_;
import cds.gen.sap.cap.teamcenter.Bolle;
import cds.gen.sap.cap.teamcenter.ToTBolle;
import io.vavr.collection.Stream;

import com.sap.cds.Result;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.cqn.CqnInsert;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsEntity;
import com.sap.cds.reflect.CdsModel;
import com.sap.cap.teamcenter.*;
import com.sap.cds.services.EventContext;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CdsReadEventContext;
import cds.gen.adminservice.Bolle_;
import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import static com.sap.cds.Struct.access;
import static com.sap.cds.Struct.stream;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 @Component
 @ServiceName("AdminService") // perché adminService si chiama il servizio definito in admin-service.cds
public class customHandler implements EventHandler {

    @Autowired
    PersistenceService db; // get access to the service

//    @Autowired
//    EventContext genericContext;


    private static Logger Log = LoggerFactory.getLogger(customHandler.class);

    //private Map<Object, Map<String, Object>> bolle = new HashMap<>();

    /*
     @On(event = CdsService.EVENT_CREATE, entity = "AdminService.Bolle")
     public void onCreate(CdsCreateEventContext context) {
         Log.warn("Log On Create.....");
         context.getCqn().entries().forEach(e -> bolle.put(e.get("Bolla"), e));
         context.setResult(context.getCqn().entries());
     }
    @On(event = CdsService.EVENT_READ, entity = "AdminService.Bolle")
     public void onRead(CdsReadEventContext context) {
         Log.warn("Log On Read.....");
         context.setResult(bolle.values());
     }
     
    
     @Before(event = CdsService.EVENT_CREATE, entity = "*")
     public void splitBolle( List<ToTBolle> bolle )
     {
        Log.info("Log Before Create.....");
        Log.info("List of Bolle size: " + bolle.size());
       // bolle.forEach(b -> checkBolla(b));
     }
*/
    @On(event = CdsService.EVENT_CREATE, entity = "AdminService.ToTBolle")
    //@On(service = "AdminService", event = CdsService.EVENT_CREATE)
     public void processBolle(EventContext genericContext, ToTBolle bolle )
     {
        CdsCreateEventContext context = genericContext.as(CdsCreateEventContext.class);
        List<Map<String, Object>> listBolle = new LinkedList<>();
        Log.info("Log On Create.....");
        Log.info("List of Bolle size: " + bolle.size());
        List<Map<String,Object>> item = (List<Map<String,Object>>) bolle.get("item");
       // Stream<ToTBolleItem> tot = stream(item).as(ToTBolleItem.class);
       List<ToTBolleItem> tot = stream(item).as(ToTBolleItem.class).collect(Collectors.toList());
       // bolle.forEach(b -> checkBolla(b));
       List<String> listBolleOK = new ArrayList<>();
       //String sCreatedAt = bolle.CREATED_AT.toString();
       tot.forEach(
           bolla -> { 
                        String bollaNum = bolla.getBolla();
                        Log.info("NUmero Bolla " + bollaNum); 
                        // Query Builder API
                        CqnSelect query = Select.from(Bolle_.class)
                                                .columns(i -> i.Bolla(), i -> i.Odl())
                                                .where(b -> b.Bolla().eq( bollaNum ) );

                        Long rowCOunt = db.run(query).rowCount();
                        Log.info("RowCount " + rowCOunt); 
                        
                        if(rowCOunt == 0)
                        {
                            listBolleOK.add(bolla.getBolla());
                            Map<String, Object> singleBolla = new HashMap<>();
                            singleBolla.put("Bolla", bolla.getBolla());
                            singleBolla.put("PartNumber", bolla.getPartNumber());
                            singleBolla.put("Odl", bolla.getOdl());
                            listBolle.add(singleBolla);
                            Log.info("Inserting Bolla: " + bolla.getBolla());                               
                        }
                       
                    } 
       );
        Result result = null;
        CqnInsert insert = null;
        if( listBolle.size() > 0 )
                        {
                            insert = Insert.into(Bolle_.class).entries(listBolle);
                            result = db.run(insert);
                            result.forEach( row ->
                                Log.info("Inserted Bolla: " + row.get("Bolla"))
                            );
                        }

        // CqnInsert insertContextOut = context.getCqn().entries().stream().
        List<Map<String, Object>> listContextIn = (List) context.getCqn().entries().get(0).get("item");
        List<Map<String, Object>> listItemOut = listContextIn.stream()
                                                                .filter(bolla -> listBolleOK.contains(bolla.get("Bolla").toString() ) )
                                                                .collect(Collectors.toList());
        Map<String, List> mapOut = new HashMap();
        mapOut.put("item", listItemOut);
        List<Map<String, ?>> listOut = new ArrayList<>();
        listOut.add(mapOut);
        CqnInsert insertContextOut = Insert.into(ToTBolle_.class).entries(listOut);                 
        Log.info("Exit On Create.....");
        context.setCqn(insertContextOut);
        context.setResult(context.getCqn().entries());
   //    context.setResult(result);
     }

    private Object checkBolla(Bolle b) {
        return null;
    }
    
    /*
    @PostMapping
    public void registerStudent(@RequestBody List<Bolle> bolle) // gli diciamo di prendere lo student dalla request
    {
        Log.info("Log PostMapping.....");
        Log.info("List of Bolle size: " + bolle.size());
    }
    */
    
}
