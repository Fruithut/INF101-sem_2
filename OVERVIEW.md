# Oversikt
Student: Olav Gjerde
Brukernavn: ogj005
E-mail: fruithut@gmail.com / ogj005@student.uib.no

## Generelle endringer angående orginal struktur og kode

    ISimObject:
      *La til decreaseHealth(), decreaseHealth(double amount), increaseHealth(), double getHealth()
        -> AbstractSimObject implementerer disse med 'default' verdier. (det vil si at der er og en health-variabel)
        
    AbstractMovingObject:
       *Utvidet klassen med nye feltvariabler (se: klassehierarkiet) siden det var felles for nye
        klasser og ikke kom i konflikt med oppførsel og struktur fra del 1.
       *Generer tilfeldig direction hver 200 steps -> for bruk i subklasser

    SimAnimal: 
      *Grafikk vil rotere når en viss vinkel er nådd og grafikk for å vise "liv".
      *Grafikk for "liv" og synsvinkel
      *Har nå funksjonalitet for å opprette lytter i konstruktør (legges til i habitat)
      *Konsum av IEdibleObject utløser en 'event' ("yum")
      *Forandret konstruktør til å matche nye variabel i AbstractMovingObject
      *Nye metoder: 'getClosestRepellant', 'averageRepellantAngle', 
                    'increaseHealth', 'decreaseHealth', 'getBestFood' (fungerende).
                    
    Blob:
      *Forandret konstruktør til å matche nye variabel i AbstractMovingObject
      
    Direction:
      *Nye metoder: 'angleDifference', 'toRadians'.
        
    Setup:
      *Registrerte en/flere "factory" (lambda) for å opprette nye Sim... - objekter
      *Generer meteorer og romskip etter "random"-generering
      
    Habitat:
      *Endret step() for å unngå "ConcurrentModificationError"
      
    SimMain:
      *Endret variabler anngående dimensjonene på habitatet (større + mindre side meny)
      *La til en bakgrunn i drawBackground()-metoden
      
    SimAnimalAvoidingTest:
      *Ordnet på 'avoidDangerTest1' -> denne skjekker nå om avstanden fra 'faren'
       til SimAnimal-objektet øker etter en gitt mengde steg. (tester begge)
        
## Om funksjonaliteten/prosjektet

    Klasseoversikt:
        SimHunter
        SimPrey
        SimMeteor
        SimProjectile
        SimGoldStar
        SimSilverStar
        SimObjectHelper
        
        Beskrivelse av klassehierarkiet (alt som er nytt):
            AbstractSimObject
                *SimGoldStar (IEdibleObject)
                    Feltvariabler: double ENERGY_FACTOR, DIAMETER, size
                                   int counter, expirationTimer
                *SimSilverStar (IEdibleObject)
                    Feltvariabler: ^Samme som over
                        AbstractMovingObject
                            Feltvariabler: Random randomGen
                                           Direction randomPath
                                           int stepCount
                                           Habitat habitat
                                *SimHunter
                                    Feltvariabler: double defaultSpeed (brukt i konstruktør)
                                *SimPrey
                                    Feltvariabler: double defaultSpeed
                                *SimMeteor
                                    Feltvariabler: double defaultSpeed, height, width
                                                   int type
                                *SimProjectile
                                    Feltvariabler: double defaultSpeed
                                                   int range, type
            Utenforstående
                *SimObjectHelper
                    Feltvariabler: Ingen, kun statiske hjelpemetoder
            Tester
                *xxx
                *xxx
                
        - De nye klassene bygger videre på funksjonaliteten som allerede er i AbstractSimObject og
          i AbstractMovingObject. Det har blitt lagt til metoder i ISimObject som sier at der skal
          være metoder som øker/minker 'health' o.l, en "default" implementasjon av dette har blitt laget
          i AbstractSimObject. AbstractMovingObject har blitt utvidet med 4 nye variabler, en for Random,
          Direction, (int) stepCount, og Habitat. 
          Fra toppen har vi SimGoldStar og SimSilverStar som utvider AbstractSimObject dette er IEdibleObjects
          som har en fast posisjon og som vil forsvinne etter et gitt antall steps. Næringsinnholdet er som med
          SimFeed i del 1 bestemt etter størrelsen på objektene -> og minker ved konsumering. Lavere i hierarkiet
          har vi SimHunter, SimPrey, SimMeteor og SimProjectile. Merk derimot at SimProjectile kun blir nyttet
          gjennom SimHunter og SimPrey, og eventuelt 'direkte' under testing. Utenom dette er der en hjelpeklasse
          SimObjectHelper som kun har statiske hjelpemetoder til bruk sammen med de nye klassene.
    
    Feil/mangler
        - "Ingen kritiske forhåpentligvis" - Olav :)
    
    Oppførsel/"Spilleregler"
        - SimHunter
            Mål: Unngå meteorer, konsumere SimGoldStars, og jakter på SimPrey til slutt.
            Fart: min 1 - maks 2 (unntak ved å komme seg inn til habitat igjen)
            Liv: 1 -> maks 1.5
            Prosjektil: 1/50 sjangs for å skyte når SimPrey er innen rekkevidde
                        og er i synsfeltet (180grader). Skyter type 0.
            Vanlig retning: Går mot midten så lenge avstanden til midten er større enn 900,
                            vil ta en tilfeldig retning om den er innenfor. 
            Siktavstand: 375
            
        - SimPrey
            Mål: Jakter på meteorer og øker livet sitt ved å konsumere SimSilverStars.
                 Prøver å unngå SimHunter som "siste" prioritet.
            Fart: min 1.5 - maks 2.25 (unntak ved å komme seg inn til habitat igjen)
            Liv: 1 - maks 2.0
            Prosjektil: 1/70 sjangs for å skyte når SimMeteor er innen rekkevidde
                        og er i synsfeltet (180grader). Skyter type 1.
            Vanlig retning: Går mot midten så lenge avstanden til midten er større enn 900,
                            vil ta en tilfeldig retning om den er innenfor.
            Siktavstand: 325
        
        - SimMeteor
            Mål: Blir "spawnet" utenfor habitat og kommer seilende inn. Utgjør en farefaktor.
            Fart: 1.5
            Liv: 1.0
            Retning: Får tilskrevet en tilfeldig retning under initialisering, forandrer bane ved
                     kollisjon med andre SimMeteors, SimPreys eller SimHunters.
            Ved død: Om type 0 -> exploderer til 2 mindre SimMeteors(type 1)
                     Om type 1 -> lager et SimSilverStar-object på sin posisjon
            
        - SimProjectile
            Mål: Skade objekter i sin egen bane alt etter type prosjektil.
                 Type 0 -> Skader SimPrey og eventuelt SimMeteor
                 Type 1 -> Skader SimMeteor og eventuelt SimHunter
            Fart: 3
            Retning: Som den er brukt i SimHunter og SimPrey tar den retningen som "de" var i
                     under initialisering.
        
        - SimGoldStar/SimSilverStar
            Mål: Fungere som liv til SimHunter/SimPrey
            Fart: 0 -> står stille
            Ekstra: Har en viss holdbarhet og vil begynne å blinke
                    når denne grensen nærmer seg.
    
    System
        Design
            - Planen er å lage noe som kan ligne på de gamle "space-shooters" fra Atari-tiden. 
        
        Implementasjonsvalg
            -

## Svar på spørsmål

    Svar til innledende spørsmål:
        Hvordan virker "Position.move()", og hvordan er den annerledes fra Position-klassen i Lab 5?
            -  move()-metoden som AbstractMovingObject benytter fungerer ved at den tar inn et Direction object og
            en double som representerer "distance". Distance fra simuleringen sin side er i dette tilfellet farten vi
            vil at et bestemt objekt skal opperere med. Dette er fordi move()-metoden tar inn disse variablene og oppretter
            et nytt posisjonsobjekt med en relativ bevegelse i forhold til parameterne som den mottok. I retur får vi et
            nytt posisjonsobjekt som AbstractSimObject kan sette til å være sitt nye "Position"-objekt.
            Merk at her setter vi den gamle Position-variabelen i AbstractSimObject til det nye Position-objektet som
            vart opprettet fra 'move()', dette er fordi Position-klassen er det vi kaller for "immutable" 
            og ikke kan forandre sine feltvariabler etter objektet har blitt opprettet, på denne måten forhindrer vi 
            direkte forandringer/muteringer av et Position-objekt sine feltvariabler.
                *De to andre move()-metodene skaper og relativ bevegelse utifra parameterene som blir parset inn, det er 
                bare flere varianter av metoden til å bruke etter hvilke posisjons-data man sitter med (hendig med testdata).
            Hovedforskjellen mellom denne varianten og den som finnes i Lab 5 er at Labben sin metode forandret på 
            feltvariablene til objektet og dermed muterte seg selv. Man kunne altså ikke vite etter initialisering av
            "Position"-objektet at verdiene som det vart opprettet med alltid ville være sanne (ikke invariant som i sem2).
            
        Hva er forskjellen på "AbstractSimObject" og "AbstractMovingObject"?
            - AbstractSimObject inneholder den grunnleggende logikken for objekt som skal simulerest i programmet, og 
            tilskriver objektene med egenskaper som posisjon, retning, dø/levende og lignende. Mens AbstractMovingObject
            er en arver fra AbstractSimObject og utvider denne med logikk som beror seg på bevegelse slik som akselerering, 
            farten til objektet, og hva som må brukes for å reposisjonerer objektet.
            
        Posisjonen er lagret som en private feltvariabel, hvordan skal da subklasser justere posisjonen?
            - Posisjonsdatene blir konstruert utifra hvilke data som blir gitt fra retningsdataene (les: Direction),
            for hver iterering av programmet vil et bevegende objekt få tilskrevet et nytt Position-objekt gjennom step()
            i AbstractMovingObject som bruker "reposition(..)" i kombinasjon med "Position.move(...)" (se beskrivelse av
            dette i spm.1). Etter nok "steps" vil objektet komme til ønsket posisjon ved å gå i en gitt retning (for å 
            oppnå dette kan man da gjøre dir = "nytt 'Direction'-objekt her").
            
        Hva gjør hjelpemetodene "distanceTo" og "directionTo"?
            - 'distanceTo' tar inn et Position-objekt og regner ut avstanden mellom objektet som kalte på metoden til
            den andre posisjonen som ble parset inn, og returnerer en double med denne avstanden. 'directionTo' tar også
            inn et Position-objekt, videre blir et nytt Direction-objekt returnert som beskriver vinkelen fra objektet 
            som kalte på metoden og til den gitte posisjonen (dermed 'retning' til en gitt posisjon).
            
        "AbstractMovingObject" har en metode "accelerateTo" for å endre på farten. 
        Kunne det være smart å gjøre "speed" feltvariablen private?
            - Sånn som strukturen til programmet er bygd opp fra den orginale koden kan denne variabelen likegodt vere 
            private siden den kun blir justert gjennom metodene i egen klasse, i tillegg har man en public getSpeed() 
            metode for å finne ut hvafarten er satt til uten å måtte går direkte til feltvariablen. Jevn akselerering
            ser også mer naturlig ut enn et plutselig "hopp" i fart.
        
        Der er ikke laget hjelpemetoder for å justere retningen. Hva må man gjøre for å endre retningen i en subklasse?
        Burde man ha metoder også for å endre retning?
            - Som nevnt i et tidligere spørsmål vil man for å endre retning sette Direction feltvariabelen til objektet
            til et nytt Direction-objekt. Dette gjøres på denne måten fordi Direction klassen, slik som Position klassen
            er ikke-muterbar eller "immutable". Om Direction klassen skal fortsette å vere dette kan man iallfall ikke
            ha hjelpemetoder i selve Direction klassen som forandrer på feltvariablene. Men man kunne f.eks hatt en "changeDir"
            metode i AbstractSimObject som tok inn et nytt Direction object og gjor (this.dir = "ny retning her") uten at
            man måtte repetere denne koden lavere i hierarkiet, slik som i SimAnimal.
            
        Der er ingen public metoder for å endre posisjon, retning, osv. Hadde det vært lurt å ha det? (Hvorfor / hvorfor ikke)
            - Hadde der vært public metoder i de Abstrakte klassene for å endre på retning, posisjon og lignende, ville 
            forskjellige klasser som ikke er samme "pakke" som disse kunne forandret posisjonen til objektene. Det vil si
            at man kunne gjort slik som SimAnimal.setPos(pos) og den ville flyttet seg i steden for at den nå kun forandrer
            sin posisjon og retning gjennom step()-metoden. Dette er nødvendigvis ikke noe man ønsker om hvert av objektene
            i simuleringen har egne definisjoner for hvordan de skal oppføre seg eller bevege seg og denne logikken kommer
            fram gjennom det som har blitt definert i dens "step()"-metode. Et plus er at man kanskje har fleksibiliteten
            til å kunne gjøre uventede forandringer, men denne kresjer kanskje litt med tanken om en "simulering".         
        
    Svar for 1.7 (Se bakgrunn for spørsmål i "README.md" under del 1.7):
        Hvorfor tror du vi har laget systemet på denne måten? Hadde det vært andre måter å få det til på 
        (ville det i såfall vært like fleksibelt)?
            - Det minimalistiske kode stilen gjør at man unngår unødvendig mye kode rundt om i "programmet", ved å utvide
            den allerede eksisterende kode med flere objekt holder man på den Abstrakte struktur som har blitt konstruert.
            Man går fra å ha veldig generelle egenskaper på "toppen" av hierarkiet til mer detaljrike egenskaper nederst,
            og noen av disse egenskapene kommer fra slik som komparator osv. Ved å bruke anonyme klasser unngår vi unødvendige
            separerte klasser i filsystemet som forstyrrer den enkle strukturer som er der. Man kunne i værste tilfelle
            hatt all funksjonalitet inn i hver klasse (til og med det som er felles for f.eks SimObjects) dette hadde ført
            til mye kode gjenbruk, og en generelt sett dårlig kode stil.
            
        Kunne vi fått dette til uten å bruke grensesnitt?
            - Man kunne laget samme program uten interface-klasser men det er mye enklere å kunne forholde seg til disse
            siden man vet utifra grensesnittet hva klassene må inneholde av metoder. Man kan og ha definerte forklaringer
            for hvordan metodene skal fungere i samsvar med objektet som implementerer de. På denne måten fungerer grensesnittene
            som en kontrakt som man kan forholde seg til. I tillegg måtte man uten grensesnitt definert måter å håndtere
            hvert enkelt objekt i forskjellige situasjoner, noe som hadde blitt til veldig mye kode.

## Kilder til media
    
    //TODO CREDITS FOR GRAFIKK
    
    * Rammeverkkode: © Anya Helene Bagge (basert på tidligere utgaver, laget av Anya Helene Bagge, Anneli Weiss og andre).
    * pipp.png, bakgrunn.png © Anya Helene Bagge, This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License
