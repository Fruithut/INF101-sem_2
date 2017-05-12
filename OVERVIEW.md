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
      *Forandret konstruktør til å matche ny variabel i AbstractMovingObject
      
    Direction:
      *Nye metoder: 'angleDifference', 'toRadians'.
        
    Setup:
      *Registrerte en/flere "factory" (lambda) for å opprette nye Sim... - objekter
      *Generer meteorer og romskip etter "random"-generering
      
    Habitat:
      *Endret step() for å unngå "ConcurrentModificationError"
      
    SimMain:
      *Endret variabler anngående dimensjonene på habitatet (større + mindre side meny)
      *La til en boolsk variabel for å vite om lyd effekter skal vere på (satt på fra setup)
      *La til en bakgrunn i drawBackground()-metoden
      *La til metoder for å spille av bakgrunnmusikk og å sette lydvariabel til true/false
       i tillegg en get metode for denne.
      
    SimAnimalAvoidingTest:
      *Ordnet på 'avoidDangerTest1' -> denne skjekker nå om avstanden fra 'faren'
       til SimAnimal-objektet øker etter en gitt mengde steg. (tester begge)
        
## Om funksjonaliteten/prosjektet

    System
            Design
                - Planen er å lage noe som kan ligne på de gamle "space-shooters" fra Atari-tiden. Dette er noe som jeg
                  tror kan passe godt med hvordan objektene har mulighet til å bevege seg rundt i kordinatsystemet mtp.
                  360 grader osv. For å få til dette må der være romskip, planen er å få laget 2 forskjellige skip med
                  litt forskjellige oppførsel. Jeg tenker at det ene skipet kan være et som hovedsaklig vil jakte på
                  den andre typen skip. Dette skal få være det tregeste objektet i simuleringen slik at det andre skipet
                  har mulighet for å kunne overleve. Skip nr.2 skal være mer agilt enn det første og dermed ha litt
                  raskere fart og retningjusteringer. Jeg tenkte at dette skipet ikke skal utgjøre en trussel til skip nr.1
                  direkte men kan prøve å ødlegge meteorer som kommer flygende inn, som igjen vil kunne treffe begge skipene,
                  men siden skip nr.1 er tregere vil det ha større sannsynlighet for å treffe dette. Merk derimot at begge
                  skipene har mulighet til å "se" meteorer og vil prøve å unnvike dem. Dette vil si at jeg også vil ha et 
                  objekt som representerer meteorer. Planen er at meteorer skal virke som de kommer flygende fra ingen plass
                  og har som i tidlige atari spill muligheten for å explodere til mindre meteorer. Meteorene skal og ha 
                  mulighet til å kunne kresje med hverandre og deretter forandre retning. All denneoppførselen vil kreve
                  at jeg får laget et objekt som kan oppføre seg som prosjektiler som skipene skal kunne skyte. 
                  Alle bevegende ting skal ha en livs-variabel, og vil kunne ha metoder som øk/senk liv osv. Om denne
                  utnyttes i objektene vil variere, i utgangspunktet tenker jeg at skipene og meteorene vil ha liv der det 
                  skjer noe om denne verdien treffer 0. Skip nr.1 skal bare forsvinne ved liv 0, skip nr.2 skal opprette 
                  noe som kan konsumerest av skip nr.1. SimMeteor skal eksplodere ved liv 0 (eller hard kollisjon), og om
                  meteorene som kom etter eksplosojnen dør skal det opprettes noe som kan konsumerest av skip nr.2.
                  (Se ferdig oversikt over oppførsel og regler lenger nede)
            
            Implementasjonsvalg
                - Startet med å lage en klasse som senere ble SimHunter når riktig grafikk hadde blitt lagt på. Oppførselen
                  ble definert gjennom step()-metoden og tok i bruk hjelpemetoder i en ny klasse jeg laget; SimObjectHelper.
                  Denne hjelpeklassen laget jeg etter jeg såg at SimHunter og SimPrey, som er klasser som ligner på hverandre,
                  trengte mye av samme informasjonen for å definere sin egen oppførsel. I denne klassen la jeg til statiske
                  metoder som klassene jeg hadde laget kunne bruke -> i tillegg la jeg til ekstra metoder i denne etterhvert
                  som det kom nye objekter, dette mye redundans i de forskjellige klassene. Både SimHunter og SimPrey fikk en
                  synsvinkel på 180 grader (ved hjelp av angleDifference metoden jeg implementerte i Direction), så hva som
                  "interesant" osv avhenger av avstandbegrensning og denne vinkelen. Et unntak er at SimPrey vil merke at 
                  SimHunter nærmer seg. Jeg valgte heretter å lage en prosjektil-klasse som hadde to forskjellige oppførseler
                  alt etter hvilken type man valgte i konstruktøren, dette gjor jeg slik at SimPrey og SimHunter kunne ha
                  ulike "ammunisjon" som skadet ulikt. Da unngår jeg at prosjektilene fra SimHunter skader seg selv og det
                  samme for SimPrey. Jeg valgte også her å integrere en rekkevidde slik at "kamp områdene" ble litt mer
                  innesperret og at disse objektene ikke skadet ting på andre siden av "habitat". Så laget jeg SimMeteor
                  klassen, her laget jeg flere konstruktører slik at jeg kunne få en til å bruke ved eksplosjonseffekten
                  jeg ville implementere, en for bruk ved testing og en som generer ganske tilfeldige retninger og plasseringer
                  utifra dimensjonene på "habitat". For å kunne få disse til å kresje med andre objekt la jeg til nye
                  hjelpemetoder i SimObjectHelper slik at klassen nå fikk mulighet til å vite nærmeste romskip og meteor.
                  Felles for både SimProjectile og SimMeteor er at de kaller det nye "decreaseHealth(..)" metodene på
                  objektene de treffer, dette gjor jeg slik at ikke både den som "skal" treffe og den som blir truffet
                  må holde rede på det. Dette lettet kodearbeidet og gjor ting ryddigere. Til slutt laget jeg SimGoldStar
                  og SimSilverStar slik at både SimHunter og SimPrey hadde sine separate ting å kunne overleve på. Her la
                  jeg til en teller som holder rede på hvor lang "holdbarhet" det skal være på objektet -> slik at det ikke
                  blir mye oppsamlet i habitat over tid (gjør ting litt vanskligere for romskipene). Jeg å forandre litt
                  på dimensjonene i SimMain for å få litt større "pusterom" noe som såg naturlig ut i noe som skal være i 
                  verdensrommet. SimSounds klassen ble tilpasset fra koden jeg laget til semoppg 1, og prøver å laste inn
                  lydfiler ved initialisering. (Ekstra dokumentasjon finnes i klassene og under)

    Klasseoversikt:
        Objects in simulation:
        SimHunter
        SimPrey
        SimMeteor
        SimProjectile
        SimGoldStar
        SimSilverStar
        
        Helper:
        SimObjectHelper
        SimSounds
        
        Tests:
        SimHunterTest
        SimPreyTest
        SimMeteorTest
        SimProjectileTest
        SimGoldStarTest
        SimSilverStarTest
        
    Beskrivelse av klassehierarkiet (alt som er nytt):
        AbstractSimObject
            Feltvariabler: double health
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
                                                   int deathTimer (animasjonsrelatert)
                                *SimPrey
                                    Feltvariabler: double defaultSpeed
                                                   int deathTimer (animasjonsrelatert)
                                *SimMeteor
                                    Feltvariabler: double defaultSpeed, height, width
                                                   int type, hitCount (lydrelatert)
                                *SimProjectile
                                    Feltvariabler: double defaultSpeed
                                                   int range, type
        Utenforstående
            *SimObjectHelper
                Feltvariabler: Ingen, kun statiske hjelpemetoder
            *SimSounds
                Laster inn lydfiler til bruk i simuleringen
        Tester
            *SimPreyTest
            *SimHunterTest
            *SimMeteorTest
            *SimProjectileTest
            *SimGoldStarTest
            *SimSilverStarTest
                
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
          SimObjectHelper som kun har statiske hjelpemetoder til bruk sammen med de nye klassene. Der er også en
          SimSounds klasse som prøver å laste lydfiller når klassen blir initialisert.
    
    Feil/mangler
        - "Ingen kritiske forhåpentligvis" - Olav :)
    
    Oppførsel/"Spilleregler"
        - SimHunter
            Mål: Unngå meteorer, konsumere SimGoldStars, og jakter på SimPrey til slutt.
            Fart: min 2 - maks 3 (unntak ved å komme seg inn til habitat igjen)
            Liv: 1 -> maks 1.5
            Prosjektil: 1/50 sjangs for å skyte når SimPrey er innen rekkevidde
                        og er i synsfeltet (180grader). Skyter type 0.
                        Prosjektilrekkevidde -> 275
            Vanlig retning: Går mot midten så lenge avstanden til midten er større enn 900,
                            vil ta en tilfeldig retning om den er innenfor. 
            Siktavstand: 375
            
        - SimPrey
            Mål: Jakter på meteorer og øker livet sitt ved å konsumere SimSilverStars.
                 Prøver å unngå SimHunter som "siste" prioritet.
            Fart: min 2.5 - maks 3.75 (unntak ved å komme seg inn til habitat igjen)
            Liv: 1 - maks 2.0
            Prosjektil: 1/70 sjangs for å skyte når SimMeteor er innen rekkevidde
                        og er i synsfeltet (180grader). Skyter type 1.
                        Prosjektilrekkevidde -> 300
            Vanlig retning: Går mot midten så lenge avstanden til midten er større enn 900,
                            vil ta en tilfeldig retning om den er innenfor.
            Siktavstand: 325
        
        - SimMeteor
            Mål: Blir opprettet utenfor habitat og kommer seilende inn. Utgjør en farefaktor.
            Fart: 2.75
            Liv: 1.0
            Retning: Får tilskrevet en tilfeldig retning under initialisering, forandrer bane ved
                     kollisjon med andre SimMeteors, SimPreys eller SimHunters.
            Ved død: Om type 0 -> exploderer til 2 mindre SimMeteors(type 1)
                     Om type 1 -> lager et SimSilverStar-object på sin posisjon
            
        - SimProjectile
            Mål: Skade objekter i sin egen bane alt etter type prosjektil.
                 Type 0 -> Skader SimPrey og eventuelt SimMeteor
                 Type 1 -> Skader SimMeteor og eventuelt SimHunter
            Fart: 4
            Retning: Som den er brukt i SimHunter og SimPrey tar den retningen som "de" var i
                     under initialisering.
        
        - SimGoldStar/SimSilverStar
            Mål: Fungere som liv til SimHunter/SimPrey
            Fart: 0 -> står stille
            Ekstra: Har en viss holdbarhet og vil begynne å blinke
                    når denne grensen nærmer seg.

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
            farten til objektet, og hva som må brukes for å reposisjonere objektet.
            
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
            som en kontrakt som man kan forholde seg til. Man kan og senere velge hvilke klasser som oppfyller kravene til grensesnittene.
            I tillegg måtte man uten grensesnitt definert måter å håndtere hvert enkelt objekt i forskjellige situasjoner, 
            noe som hadde blitt til veldig mye kode.

## Kilder til media

   **Lydeffekter og musikk**
        
        Dreamy Flashback Kevin MacLeod (incompetech.com)
        Licensed under Creative Commons: By Attribution 3.0 License
        http://creativecommons.org/licenses/by/3.0/
        
        "hunterCrash" - By n_audioman (https://freesound.org/people/n_audioman/)
        License: https://creativecommons.org/licenses/by/3.0/
        
        "laser1" - By ani_music (https://freesound.org/people/ani_music/)
        License: https://creativecommons.org/licenses/by/3.0/
        
        "laser1Failure" - By moca (https://freesound.org/people/moca/)
        License: https://creativecommons.org/publicdomain/zero/1.0/
        
        "laser2" - By ani_music (https://freesound.org/people/ani_music/)
        License: https://creativecommons.org/licenses/by/3.0/
        
        "meteorExplode" - By smcameron (https://freesound.org/people/smcameron/)
        License: https://creativecommons.org/licenses/by/3.0/
        
        "meteorHit1" - By Reitanna (https://freesound.org/people/Reitanna/)
        License: https://creativecommons.org/publicdomain/zero/1.0/
        
        "meteorHit2" - By Reitanna (https://freesound.org/people/Reitanna/)
        License: https://creativecommons.org/publicdomain/zero/1.0/
        
        "meteorHitShip" - By OwlStorm (https://freesound.org/people/OwlStorm/)
        License: https://creativecommons.org/licenses/by/3.0/
        
        "preyCrash" - By n_audioman (https://freesound.org/people/n_audioman/)
        License: https://creativecommons.org/publicdomain/zero/1.0/

   **Grafikk:**

        "Space Shooter Redux" By Kenney (http://www.kenney.nl).
        License: https://creativecommons.org/publicdomain/zero/1.0/
        
        "Space Shooter Extension (250+)" By Kenney (http://www.kenney.nl).
        License: https://creativecommons.org/publicdomain/zero/1.0/
        
        
   **Ekstra**
    
        * Rammeverkkode: © Anya Helene Bagge (basert på tidligere utgaver, laget av Anya Helene Bagge, Anneli Weiss og andre).
        * pipp.png, bakgrunn.png © Anya Helene Bagge, This work is licensed under a Creative Commons Attribution-ShareAlike 4.0 International License
