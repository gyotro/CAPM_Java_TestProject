namespace sap.cap.teamcenter;

/** 
*    con questa istruzione si dichiarano le strutture standard 
*   che saranno utilizzate (ad esempio managed è un aspect)
*/ 
using { managed, sap } from '@sap/cds/common';  


entity seriali_eventi : managed {

    key Bolla               : String;
        ODL                 : String;
        SEQ                 : String;
        EventType           : String;
        MsgText             : String;
        Operatore_OP_ID     : String;
        SerialNumber        : Integer64;
        Partita             : String;
        Machine             : String;
        EventDate           : Date
    
}

entity measures : managed {

        key Bolla                   : String;
            PartNumber              : String;
            SerialNumber            : Integer64;
            QAG_Description         : String;
            Nome_Caratteristica     : String;
            UoM                     : String;
            ValNominale             : Double;
            LowerLimit              : Double;
            UpperLimit              : Double;
            Value                   : Double;
            Operatore               : String;
            DataMisura              : Date

}


entity bolle : managed {

        key Bolla                   : String;
            PartNumber              : String;
            ActualStartTime         : Time;
            ActualEndTime           : Time; 
            Odl                     : String;
            Seq                     : Integer;
            OpType                  : String;
            Cam                     : String;

}


entity part_program : managed {

        key Bolla                   : String;
            PartProgram             : String;
            Machine                 : Time;
}

type bolla {

            Bolla                   : String;
            PartNumber              : String;
            ActualStartTime         : Time;
            ActualEndTime           : Time; 
            Odl                     : String;
            Seq                     : Integer;
            OpType                  : String;
            Cam                     : String;

}


entity ToTBolle : managed { item : array of { 

            Bolla                   : String;
            PartNumber              : String;
            ActualStartTime         : Time;
            ActualEndTime           : Time; 
            Odl                     : String;
            Seq                     : Integer;
            OpType                  : String;
            Cam                     : String;


    } 
 }


