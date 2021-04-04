using { sap.cap.teamcenter as tc } from '../db/schema';
service AdminService @(_requires: 'admin')      // questo è il nome del servizio che verrà richiamato dall'Handler Java
                     @(path: '/teamcenter') 
{
    entity Bolle as projection on tc.bolle;
    entity PartProgram as projection on tc.part_program;
    entity SerialiEventi as projection on tc.seriali_eventi;
    entity Measures as projection on tc.measures
}   