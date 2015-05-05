#NOTE: The first time you will not DROP the database because it doesn't exist, but every time after you will have to.
#DROP DATABASE Datastellar;

CREATE DATABASE Datastellar;
Use Datastellar;
CREATE TABLE Galaxy(
	name VARCHAR(20),
    shape VARCHAR(20),
    diameter INT,
    PRIMARY KEY (name)
);
CREATE TABLE Star(
	name VARCHAR(20),
    mass INT,
    class VARCHAR(20),
	inGalaxy VARCHAR(20),
    FOREIGN KEY(inGalaxy) REFERENCES Galaxy(name),
    PRIMARY KEY (name)
);
CREATE TABLE Planet(
	name VARCHAR(20),
    mass INT,
	orbitsStar VARCHAR(20),
	orbitalPeriod INT,
    population BIGINT,
	inGalaxy VARCHAR(20),
    FOREIGN KEY(orbitsStar) REFERENCES Star(name),
    FOREIGN KEY(inGalaxy) REFERENCES Galaxy(name),
    PRIMARY KEY (name)
);
CREATE TABLE Moon(
	name VARCHAR(20),
    mass INT,
	orbitsPlanet VARCHAR(20),
    orbitalPeriod INT,
	inGalaxy VARCHAR(20),
    FOREIGN KEY(orbitsPlanet) REFERENCES Planet(name),
    FOREIGN KEY(inGalaxy) REFERENCES Galaxy(name),
    PRIMARY KEY (name)
);
CREATE TABLE Species(
	name VARCHAR(20),
    height INT,
    numberLiving BIGINT,
    hostility INT,
    PRIMARY KEY (name)
);
CREATE TABLE Inhabits(
	speciesName VARCHAR(20),
	planetName VARCHAR(20),
    FOREIGN KEY(speciesName) REFERENCES Species(name),
    FOREIGN KEY(planetName) REFERENCES Planet(name),
    PRIMARY KEY (speciesName,planetName)
);

INSERT INTO Galaxy VALUE("Milky Way","Spiral",10000);
INSERT INTO Galaxy VALUE("Andromeda","Elliptical",21000);
INSERT INTO Galaxy VALUE("Whirlpool Galaxy","Spiral",15000);
INSERT INTO Galaxy VALUE("NGC 1300","Lenticular",25000);
INSERT INTO Galaxy VALUE("Hoags Object Galaxy","Irregular",21000);
INSERT INTO Galaxy VALUE("Centaurus A","Irregular",999000);
INSERT INTO Galaxy VALUE("Centaurus B","Elliptical",900000);
INSERT INTO Galaxy VALUE("Canis Major Dwarf","Lenticular",100000);
INSERT INTO Star VALUE("Sol",741360,"Yellow Giant","Milky Way");
INSERT INTO Planet VALUE("Earth",9999,"Sol",365,6000000000,"Milky Way");
INSERT INTO Planet VALUE("Venus",8000,"Sol",200,0,"Milky Way");
INSERT INTO Planet VALUE("Mercury",8000,"Sol",200,0,"Milky Way");
INSERT INTO Planet VALUE("Mars",9900,"Sol",1200,0,"Milky Way");
INSERT INTO Planet VALUE("Jupiter",10000000,"Sol",2200,0,"Milky Way");
INSERT INTO Planet VALUE("Neptune",8000,"Sol",2100,0,"Milky Way");
INSERT INTO Planet VALUE("Uranus",8100,"Sol",200,0,"Milky Way");
INSERT INTO Planet VALUE("Saturn",80000,"Sol",2000,0,"Milky Way");
INSERT INTO Moon VALUE("Luna",100,"Earth",10,"Milky Way");
INSERT INTO Species VALUE("Human",6,6000000000,1);
INSERT INTO Inhabits VALUE("Human","Earth");
INSERT INTO Star VALUE("Eridanus",741360,"Orange Giant","Andromeda");
INSERT INTO Star VALUE("Cassiopeia",116447,"White Hypergiant","Whirlpool Galaxy");
INSERT INTO Star VALUE("Leo",54491,"Red Giant","Hoags Object Galaxy");
INSERT INTO Star VALUE("Taurus",332854,"Brown Dwarf","Whirlpool Galaxy");
INSERT INTO Star VALUE("Lyra",187473,"Red Dwarf","Canis Major Dwarf");
INSERT INTO Star VALUE("Crux",588844,"Yellow Subgiant","Milky Way");
INSERT INTO Star VALUE("Cancer",317241,"Red Supergiant","Whirlpool Galaxy");
INSERT INTO Star VALUE("Scorpius",24888,"Red Supergiant","Whirlpool Galaxy");
INSERT INTO Star VALUE("Cepheus",824652,"Red Dwarf","Milky Way");
INSERT INTO Star VALUE("Grus",977882,"Yellow Giant","Milky Way");
INSERT INTO Star VALUE("Draco",219745,"Red Supergiant","Milky Way");
INSERT INTO Star VALUE("Perseus",62387,"Brown Dwarf","Whirlpool Galaxy");
INSERT INTO Star VALUE("Bootes",178125,"Red Supergiant","Centaurus B");
INSERT INTO Star VALUE("Crater",442635,"Red Supergiant","Centaurus A");
INSERT INTO Star VALUE("Orion",324555,"Red Supergiant","Centaurus A");
INSERT INTO Star VALUE("Gemeni",563691,"Red Dwarf","Hoags Object Galaxy");
INSERT INTO Star VALUE("Auriga",132112,"White Dwarf","Centaurus B");
INSERT INTO Star VALUE("Pegasus",230877,"Brown Dwarf","Centaurus A");
INSERT INTO Star VALUE("Aquarius",307207,"Orange Giant","Andromeda");
INSERT INTO Star VALUE("Carina",799737,"Red Giant","Andromeda");
INSERT INTO Star VALUE("Lepus",472387,"Orange Giant","Centaurus A");
INSERT INTO Star VALUE("Virgo",125258,"Red Giant","Milky Way");
INSERT INTO Star VALUE("Capricorn",563451,"White Hypergiant","NGC 1300");
INSERT INTO Star VALUE("Denebola",941118,"Red Supergiant","Centaurus A");
INSERT INTO Star VALUE("Leonis",541457,"Red Supergiant","Whirlpool Galaxy");
INSERT INTO Star VALUE("Dheneb",212580,"Brown Dwarf","Hoags Object Galaxy");
INSERT INTO Star VALUE("Etaceti",454135,"Red Dwarf","Canis Major Dwarf");
INSERT INTO Star VALUE("Diade",713773,"Yellow Giant","Hoags Object Galaxy");
INSERT INTO Star VALUE("Diphda",507905,"Yellow Giant","Andromeda");
INSERT INTO Star VALUE("Dnoces",680243,"Red Giant","Andromeda");
INSERT INTO Star VALUE("Ursae",997223,"Orange Giant","Milky Way");
INSERT INTO Star VALUE("Dschubba",723514,"Red Supergiant","Whirlpool Galaxy");
INSERT INTO Star VALUE("Scorpii",381957,"Red Giant","Centaurus A");
INSERT INTO Star VALUE("Dubhe",632371,"Red Supergiant","Milky Way");
INSERT INTO Planet VALUE("Hoth",678319,"Scorpius",653,9313067,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Tatooine",235297,"Perseus",688,3501730,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Dagobah",13298,"Orion",21,9247557,"Centaurus A");
INSERT INTO Planet VALUE("Taris",240641,"Scorpii",290,0,"Centaurus A");
INSERT INTO Planet VALUE("Manaan",411730,"Lepus",865,6654004,"Centaurus A");
INSERT INTO Planet VALUE("Korriban",362762,"Dubhe",792,0,"Milky Way");
INSERT INTO Planet VALUE("Tallon",433049,"Dschubba",245,977710,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Athens",993862,"Carina",294,8289517,"Andromeda");
INSERT INTO Planet VALUE("Salamis",502671,"Lyra",223,0,"Canis Major Dwarf");
INSERT INTO Planet VALUE("Proteus",837871,"Scorpius",835,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Nausicaa",112913,"Auriga",357,4342478,"Centaurus B");
INSERT INTO Planet VALUE("Circe",327958,"Pegasus",440,0,"Centaurus A");
INSERT INTO Planet VALUE("Pharos",668508,"Taurus",29,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Gagarin",921790,"Etaceti",441,6268098,"Canis Major Dwarf");
INSERT INTO Planet VALUE("Junthor",30078,"Virgo",941,0,"Milky Way");
INSERT INTO Planet VALUE("Pressha",823791,"Perseus",702,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Rayingri",303978,"Denebola",901,4065563,"Centaurus A");
INSERT INTO Planet VALUE("Sogelrus",475712,"Gemeni",860,0,"Hoags Object Galaxy");
INSERT INTO Planet VALUE("Antirumgon",829013,"Scorpii",691,0,"Centaurus A");
INSERT INTO Planet VALUE("Grissom",236834,"Aquarius",734,0,"Andromeda");
INSERT INTO Planet VALUE("Benda",717469,"Cepheus",548,3768290,"Milky Way");
INSERT INTO Planet VALUE("Zaherux",455916,"Bootes",573,5469654,"Centaurus B");
INSERT INTO Planet VALUE("Hong",633062,"Pegasus",128,4200022,"Centaurus A");
INSERT INTO Planet VALUE("Pomal",858674,"Lepus",875,0,"Centaurus A");
INSERT INTO Planet VALUE("Casbin",465317,"Ursae",350,0,"Milky Way");
INSERT INTO Planet VALUE("Matar",4443,"Eridanus",814,9023000,"Andromeda");
INSERT INTO Planet VALUE("Theshaca",65352,"Virgo",945,5739113,"Milky Way");
INSERT INTO Planet VALUE("Treagir",84248,"Leo",477,0,"Hoags Object Galaxy");
INSERT INTO Planet VALUE("Tereshkova",33785,"Lepus",826,32943,"Centaurus A");
INSERT INTO Planet VALUE("Antibaar",696857,"Cancer",50,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Patamalrus",133794,"Leo",448,3078088,"Hoags Object Galaxy");
INSERT INTO Planet VALUE("Hunsalra",528935,"Lyra",96,9653847,"Canis Major Dwarf");
INSERT INTO Planet VALUE("Thegeuse",535924,"Dubhe",389,2975253,"Milky Way");
INSERT INTO Planet VALUE("Solmarlon",566292,"Draco",786,0,"Milky Way");
INSERT INTO Planet VALUE("Mawinor",367895,"Leonis",246,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Vamshi",438537,"Orion",477,0,"Centaurus A");
INSERT INTO Planet VALUE("Balanor",101000,"Diade",158,7504452,"Hoags Object Galaxy");
INSERT INTO Planet VALUE("Balfron",641780,"Crux",786,0,"Milky Way");
INSERT INTO Moon VALUE("Arvuna",433226,"Athens",392,"Andromeda");
INSERT INTO Moon VALUE("Bira",782764,"Thegeuse",17,"Milky Way");
INSERT INTO Moon VALUE("Budmi",68823,"Treagir",562,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Caleston",371462,"Taris",8,"Centaurus A");
INSERT INTO Moon VALUE("Cyrene",22351,"Rayingri",936,"Centaurus A");
INSERT INTO Moon VALUE("Europa",256903,"Hong",753,"Centaurus A");
INSERT INTO Moon VALUE("Franklin",111170,"Solmarlon",188,"Milky Way");
INSERT INTO Moon VALUE("Kopis",182618,"Junthor",711,"Milky Way");
INSERT INTO Moon VALUE("Lethe",803829,"Tereshkova",357,"Centaurus A");
INSERT INTO Moon VALUE("Menae",743815,"Grissom",817,"Andromeda");
INSERT INTO Moon VALUE("Nanus",35795,"Junthor",536,"Milky Way");
INSERT INTO Moon VALUE("Oliveira",20124,"Pressha",471,"Whirlpool Galaxy");
INSERT INTO Moon VALUE("Oltan",70910,"Dagobah",51,"Centaurus A");
INSERT INTO Moon VALUE("Presrop",609974,"Casbin",627,"Milky Way");
INSERT INTO Moon VALUE("Sinmara",254063,"Vamshi",727,"Centaurus A");
INSERT INTO Moon VALUE("Solcrum",369526,"Tallon",652,"Whirlpool Galaxy");
INSERT INTO Moon VALUE("Titan",719818,"Matar",279,"Andromeda");
INSERT INTO Moon VALUE("Torfan",526868,"Athens",321,"Andromeda");
INSERT INTO Moon VALUE("Triton",60380,"Patamalrus",786,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Voya",972693,"Patamalrus",522,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Yando",827298,"Pomal",367,"Centaurus A");
INSERT INTO Moon VALUE("Endor",952534,"Pharos",149,"Whirlpool Galaxy");
INSERT INTO Species VALUE("Asari",4,442378,4);
INSERT INTO Species VALUE("Drell",10,422613,10);
INSERT INTO Species VALUE("Elcor",2,104077,1);
INSERT INTO Species VALUE("Hanar",5,330155,10);
INSERT INTO Species VALUE("Salarian",10,726498,3);
INSERT INTO Species VALUE("Turian",8,956899,10);
INSERT INTO Species VALUE("Volus",8,871527,5);
INSERT INTO Species VALUE("Bantha",8,7821,4);
INSERT INTO Species VALUE("Barabel",8,124529,2);
INSERT INTO Species VALUE("Bith",7,535362,3);
INSERT INTO Species VALUE("Bothan",8,669099,2);
INSERT INTO Species VALUE("Drall",7,513187,3);
INSERT INTO Species VALUE("Dressellian",11,156288,9);
INSERT INTO Species VALUE("Droch",3,191341,1);
INSERT INTO Species VALUE("Drovian",5,283923,8);
INSERT INTO Species VALUE("Ithroian",11,484278,6);
INSERT INTO Species VALUE("Ewok",9,228078,10);
INSERT INTO Species VALUE("Ixll",10,35169,10);
INSERT INTO Species VALUE("Jawa",11,50041,3);
INSERT INTO Species VALUE("Jenet",3,288978,2);
INSERT INTO Species VALUE("Kamarian",3,642732,2);
INSERT INTO Species VALUE("Ortolan",5,953262,8);
INSERT INTO Species VALUE("Oswaft",4,843242,5);
INSERT INTO Species VALUE("Priapulin",10,29554,10);
INSERT INTO Species VALUE("Psadan",10,788257,8);
INSERT INTO Species VALUE("Pterosaur",7,648624,6);
INSERT INTO Species VALUE("Purella",4,288907,4);
INSERT INTO Species VALUE("Pydyrian",2,981633,2);
INSERT INTO Species VALUE("Qom",11,74211,10);
INSERT INTO Species VALUE("Quarren",8,760805,6);
INSERT INTO Species VALUE("Ranat",11,858675,1);
INSERT INTO Species VALUE("Rancor",9,698243,4);
INSERT INTO Species VALUE("Ruurian",4,396823,10);
INSERT INTO Species VALUE("Sarlacc",11,405473,7);
INSERT INTO Species VALUE("Taurill",9,880461,9);
INSERT INTO Species VALUE("Thernbee",6,616346,10);
INSERT INTO Species VALUE("Trianii",2,411118,1);
INSERT INTO Species VALUE("Tynnan",3,175525,7);
INSERT INTO Species VALUE("Ugnaught",9,103408,3);
INSERT INTO Species VALUE("Twilek",5,24365,8);
INSERT INTO Species VALUE("Wookie",9,38726,9);
INSERT INTO Inhabits VALUE("Asari","Hoth");
INSERT INTO Inhabits VALUE("Rancor","Tatooine");
INSERT INTO Inhabits VALUE("Ithroian","Dagobah");
INSERT INTO Inhabits VALUE("Quarren","Manaan");
INSERT INTO Inhabits VALUE("Priapulin","Tallon");
INSERT INTO Inhabits VALUE("Wookie","Tallon");
INSERT INTO Inhabits VALUE("Sarlacc","Athens");
INSERT INTO Inhabits VALUE("Purella","Athens");
INSERT INTO Inhabits VALUE("Barabel","Athens");
INSERT INTO Inhabits VALUE("Thernbee","Nausicaa");
INSERT INTO Inhabits VALUE("Bith","Gagarin");
INSERT INTO Inhabits VALUE("Barabel","Gagarin");
INSERT INTO Inhabits VALUE("Jenet","Rayingri");
INSERT INTO Inhabits VALUE("Rancor","Rayingri");
INSERT INTO Inhabits VALUE("Purella","Rayingri");
INSERT INTO Inhabits VALUE("Ortolan","Benda");
INSERT INTO Inhabits VALUE("Bothan","Benda");
INSERT INTO Inhabits VALUE("Pterosaur","Zaherux");
INSERT INTO Inhabits VALUE("Drell","Hong");
INSERT INTO Inhabits VALUE("Ixll","Hong");
INSERT INTO Inhabits VALUE("Pydyrian","Hong");
INSERT INTO Inhabits VALUE("Ugnaught","Matar");
INSERT INTO Inhabits VALUE("Barabel","Theshaca");
INSERT INTO Inhabits VALUE("Jawa","Theshaca");
INSERT INTO Inhabits VALUE("Taurill","Theshaca");
INSERT INTO Inhabits VALUE("Wookie","Tereshkova");
INSERT INTO Inhabits VALUE("Asari","Tereshkova");
INSERT INTO Inhabits VALUE("Elcor","Patamalrus");
INSERT INTO Inhabits VALUE("Sarlacc","Hunsalra");
INSERT INTO Inhabits VALUE("Ithroian","Hunsalra");
INSERT INTO Inhabits VALUE("Volus","Thegeuse");
INSERT INTO Inhabits VALUE("Turian","Balanor");
INSERT INTO Inhabits VALUE("Hanar","Balanor");
INSERT INTO Inhabits VALUE("Salarian","Nausicaa");
INSERT INTO Inhabits VALUE("Bantha","Zaherux");
INSERT INTO Inhabits VALUE("Drall","Athens");
INSERT INTO Inhabits VALUE("Dressellian","Hunsalra");
INSERT INTO Inhabits VALUE("Droch","Athens");
INSERT INTO Inhabits VALUE("Drovian","Hunsalra");
INSERT INTO Inhabits VALUE("Ewok","Dagobah");
INSERT INTO Inhabits VALUE("Kamarian","Benda");
INSERT INTO Inhabits VALUE("Oswaft","Thegeuse");
INSERT INTO Inhabits VALUE("Psadan","Balanor");
INSERT INTO Inhabits VALUE("Qom","Zaherux");
INSERT INTO Inhabits VALUE("Ranat","Hong");
INSERT INTO Inhabits VALUE("Ruurian","Patamalrus");
INSERT INTO Inhabits VALUE("Trianii","Tallon");
INSERT INTO Inhabits VALUE("Tynnan","Tereshkova");
INSERT INTO Inhabits VALUE("Twilek","Manaan");
