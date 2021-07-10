# Funkcionális követelmények
A marketing részleg egy sor statisztikai adatot szeretne megszerezni a fizetésekről a weboldalon keresztül.
A marketing részleg által igényelt mutatók:  
    - a tranzakcióértékek átlaga;  
    - azon tranzakciók száma, amelyeknek értéke egyenlő vagy nem meghaladta az 5,000 lejt;
    - azon tranzakciók száma, amelyeknek értéke meghaladta az 5,000 lejt;
    - azon tranzakciók száma, amelyeket intéző személy nem nagykorú;
    - azon tranzakciók száma, amelyeket intéző személy Bukarestben született és román állampolgár;
    - idegen nemzetiségű személyek száma, akik tranzakciót intéztek;
Milyen adatokat fog kapni a programozó?
    - a tranzakciót intéző személy CNP-je;
    - a tranzakció értéke;

# Technikai követelmények
A marketing részleg által igényelt adatok megszerzése azokból az adatokból, amelyeket a 
kereskedelmi részleg a programozó számára biztosít.
Tehát, mivel megtörténhet, hogy a CNP hibás, ezért a marketing részleg által igényelt adatok mellett
a feldolgozási hibák is megjelenjenek.

# Bemeneti adatforma
CSV állományban ';'-al elválasztott értékek a következő formátumban:
    `1930107189436;15324.98  
    2961022513249;1860  
    1960608409971;18.1`
Minden sor egy tranzakció, első tag a CNP, második a tranzakció értéke.
Lehetnek üres sorok, ezeket kikell hagyni, a számban a tizedesrész pedig '.'-al van elválasztva.

# Kimeneti adatforma
Egyrészt, az alkalmazás biztosítani fogja a marketing részleg által igényelt adatokat, valamint
a talált hibákat is, a következő rendezett formában:
    `PayMetrics {

    averagePaymentAmount: BigDecimal,  

    smallPayments: Integer,  

    bigPayments: Integer,  

    paymentsByMinors: Integer,  

    totalAmountCapitalCity: BigDecimal,  

    foreigners: Integer,  

    errors: List of {  

        line: Integer,  

        type: Integer  

    }  
}  `

# Kiegészítő követelmények
Alapvetően a legfontosabb, hogy készíteni kell egy CNP validatort, egy különálló API-ban.

# Részletes követelmények
Minden osztály és interfész egy sub-packet-e kell legyen a 'ro.axonsoft.internship21'-nek:
    - minden ami a CNP validációra vonatkozik, az a 'cnp' sub-packetben;
    - minden ami a fizetések feldolgozására vonatkozik a 'pay'-ben;

A CnpValidator interfész implementációja egy különálló osztályban legyen 'CnpValidatorImpl' néven, ugyanabban a csomagban.
Hasonlóan kell eljárni a fizetési feldolgozás esetén is.
