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
INSERT INTO Star VALUE("Eridanus",741360,"Orange Giant","Andromeda");
INSERT INTO Star VALUE("Cassiopeia",116447,"White Hypergiant","Whirlpool Galaxy");
INSERT INTO Star VALUE("Leo",54491,"Red Giant","Hoags Object Galaxy");
INSERT INTO Star VALUE("Taurus",332854,"Brown Dwarf","Whirlpool Galaxy");
INSERT INTO Star VALUE("Lyra",187473,"Red Dwarf","Canis Major Dwarf");
INSERT INTO Star VALUE("Crux",588844,"Yellow Subgiant","Milky Way");
INSERT INTO Star VALUE("Cancer",317241,"Red Supergiant","Whirlpool Galaxy");
INSERT INTO Star VALUE("Scorpius",24888,"Red Supergiant","Whirlpool Galaxy");
INSERT INTO Star VALUE("Ursa",824652,"Red Dwarf","Milky Way");
INSERT INTO Star VALUE("Major",977882,"Yellow Giant","Milky Way");
INSERT INTO Star VALUE("Cepheus",219745,"Red Supergiant","Milky Way");
INSERT INTO Star VALUE("Grus",62387,"Brown Dwarf","Whirlpool Galaxy");
INSERT INTO Star VALUE("Draco",178125,"Red Supergiant","Centaurus B");
INSERT INTO Star VALUE("Perseus",442635,"Red Supergiant","Centaurus A");
INSERT INTO Star VALUE("Bootes",324555,"Red Supergiant","Centaurus A");
INSERT INTO Star VALUE("Crater",563691,"Red Dwarf","Hoags Object Galaxy");
INSERT INTO Star VALUE("Orion",132112,"White Dwarf","Centaurus B");
INSERT INTO Star VALUE("Gemeni",230877,"Brown Dwarf","Centaurus A");
INSERT INTO Star VALUE("Auriga",307207,"Orange Giant","Andromeda");
INSERT INTO Star VALUE("Pegasus",799737,"Red Giant","Andromeda");
INSERT INTO Star VALUE("Aquarius",472387,"Orange Giant","Centaurus A");
INSERT INTO Star VALUE("Carina",125258,"Red Giant","Milky Way");
INSERT INTO Star VALUE("Lepus",563451,"White Hypergiant","NGC 1300");
INSERT INTO Star VALUE("Virgo",941118,"Red Supergiant","Centaurus A");
INSERT INTO Star VALUE("Capricorn",541457,"Red Supergiant","Whirlpool Galaxy");
INSERT INTO Planet VALUE("Hoth",212580,"Aquarius",297,5454135,"Centaurus A");
INSERT INTO Planet VALUE("Tatooine",381267,"Crux",773,6421669,"Milky Way");
INSERT INTO Planet VALUE("Athens",517152,"Orion",789,0,"Centaurus B");
INSERT INTO Planet VALUE("Salamis",997223,"Orion",380,0,"Centaurus B");
INSERT INTO Planet VALUE("Proteus",640723,"Carina",957,484605,"Milky Way");
INSERT INTO Planet VALUE("Nausicaa",624536,"Gemeni",565,8700653,"Centaurus A");
INSERT INTO Planet VALUE("Circe",313067,"Perseus",331,6545688,"Centaurus A");
INSERT INTO Planet VALUE("Pharos",501730,"Lepus",725,7032021,"NGC 1300");
INSERT INTO Planet VALUE("Gagarin",247557,"Cassiopeia",286,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Junthor",27271,"Grus",534,2011865,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Pressha",654004,"Eridanus",984,7956792,"Andromeda");
INSERT INTO Planet VALUE("Rayingri",326428,"Orion",753,2512245,"Centaurus B");
INSERT INTO Planet VALUE("Sogelrus",977710,"Major",325,1491294,"Milky Way");
INSERT INTO Planet VALUE("Antirumgon",289517,"Leo",911,0,"Hoags Object Galaxy");
INSERT INTO Planet VALUE("Grissom",726778,"Orion",916,0,"Centaurus B");
INSERT INTO Planet VALUE("Benda",246986,"Ursa",593,1283357,"Milky Way");
INSERT INTO Planet VALUE("Zaherux",342478,"Eridanus",616,8736440,"Andromeda");
INSERT INTO Planet VALUE("Hong",912155,"Crater",645,423029,"Hoags Object Galaxy");
INSERT INTO Planet VALUE("Pomal",489275,"Cancer",633,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Casbin",268098,"Gemeni",232,4235941,"Centaurus A");
INSERT INTO Planet VALUE("Matar",417029,"Cancer",539,1620702,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Theshaca",138477,"Major",481,0,"Milky Way");
INSERT INTO Planet VALUE("Treagir",65563,"Virgo",589,1287860,"Centaurus A");
INSERT INTO Planet VALUE("Tereshkova",542872,"Taurus",315,7985691,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Antibaar",905602,"Taurus",380,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Patamalrus",753715,"Perseus",782,0,"Centaurus A");
INSERT INTO Planet VALUE("Hunsalra",768290,"Draco",560,0,"Centaurus B");
INSERT INTO Planet VALUE("Thegeuse",469654,"Bootes",494,9197128,"Centaurus A");
INSERT INTO Planet VALUE("Solmarlon",200022,"Crater",159,5463875,"Hoags Object Galaxy");
INSERT INTO Planet VALUE("Mawinor",363393,"Scorpius",460,0,"Whirlpool Galaxy");
INSERT INTO Planet VALUE("Vamshi",499872,"Leo",470,0,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Arvuna",23000,"Hong",126,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Bira",572945,"Mawinor",905,"Whirlpool Galaxy");
INSERT INTO Moon VALUE("Budmi",239393,"Circe",248,"Centaurus A");
INSERT INTO Moon VALUE("Caleston",61720,"Pressha",546,"Andromeda");
INSERT INTO Moon VALUE("Cyrene",33785,"Athens",826,"Centaurus B");
INSERT INTO Moon VALUE("Europa",32943,"Matar",872,"Whirlpool Galaxy");
INSERT INTO Moon VALUE("Franklin",839050,"Solmarlon",935,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Kopis",581416,"Junthor",794,"Whirlpool Galaxy");
INSERT INTO Moon VALUE("Lethe",732769,"Treagir",88,"Centaurus A");
INSERT INTO Moon VALUE("Menae",528935,"Patamalrus",96,"Centaurus A");
INSERT INTO Moon VALUE("Nanus",653847,"Nausicaa",324,"Centaurus A");
INSERT INTO Moon VALUE("Oliveira",970389,"Salamis",643,"Centaurus B");
INSERT INTO Moon VALUE("Oltan",539523,"Vamshi",292,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Presrop",650974,"Theshaca",155,"Milky Way");
INSERT INTO Moon VALUE("Sinmara",367895,"Antirumgon",246,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Solcrum",962457,"Patamalrus",584,"Centaurus A");
INSERT INTO Moon VALUE("Titan",776477,"Theshaca",34,"Milky Way");
INSERT INTO Moon VALUE("Torfan",486782,"Gagarin",0,"Whirlpool Galaxy");
INSERT INTO Moon VALUE("Triton",163005,"Antirumgon",452,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Voya",641780,"Nausicaa",786,"Centaurus A");
INSERT INTO Moon VALUE("Yando",752819,"Hong",751,"Hoags Object Galaxy");
INSERT INTO Moon VALUE("Endor",524392,"Benda",685,"Milky Way");
INSERT INTO Species VALUE("Asari",9,34917,4);
INSERT INTO Species VALUE("Drell",4,979616,3);
INSERT INTO Species VALUE("Elcor",10,424142,2);
INSERT INTO Species VALUE("Hanar",8,560913,4);
INSERT INTO Species VALUE("Salarian",5,387819,1);
INSERT INTO Species VALUE("Turian",10,162000,9);
INSERT INTO Species VALUE("Volus",3,386161,10);
INSERT INTO Species VALUE("Bantha",9,47037,6);
INSERT INTO Species VALUE("Barabel",9,130576,6);
INSERT INTO Species VALUE("Bith",8,127987,5);
INSERT INTO Species VALUE("Bothan",3,501336,1);
INSERT INTO Species VALUE("Drall",3,605041,5);
INSERT INTO Species VALUE("Dressellian",9,338021,4);
INSERT INTO Species VALUE("Droch",9,548768,7);
INSERT INTO Species VALUE("Drovian",4,491385,9);
INSERT INTO Species VALUE("Ithroian",11,611524,9);
INSERT INTO Species VALUE("Ewok",3,81442,1);
INSERT INTO Species VALUE("Ixll",8,814255,4);
INSERT INTO Species VALUE("Jawa",4,202357,9);
INSERT INTO Species VALUE("Jenet",9,584108,5);
INSERT INTO Species VALUE("Kamarian",11,632663,3);
INSERT INTO Species VALUE("Ortolan",9,210694,9);
INSERT INTO Species VALUE("Oswaft",4,794260,1);
INSERT INTO Species VALUE("Priapulin",8,791581,4);
INSERT INTO Species VALUE("Psadan",6,74870,9);
INSERT INTO Species VALUE("Pterosaur",9,543033,7);
INSERT INTO Species VALUE("Purella",10,957350,7);
INSERT INTO Species VALUE("Pydyrian",8,130615,7);
INSERT INTO Species VALUE("Qom",2,285124,7);
INSERT INTO Species VALUE("Quarren",10,312722,6);
INSERT INTO Species VALUE("Ranat",3,488063,7);
INSERT INTO Species VALUE("Rancor",10,634912,6);
INSERT INTO Species VALUE("Ruurian",8,337763,10);
INSERT INTO Species VALUE("Sarlacc",9,520889,2);
INSERT INTO Species VALUE("Taurill",2,226901,4);
INSERT INTO Species VALUE("Thernbee",4,115448,10);
INSERT INTO Species VALUE("Trianii",9,324356,8);
INSERT INTO Species VALUE("Tynnan",9,604150,9);
INSERT INTO Species VALUE("Ugnaught",10,912000,10);
INSERT INTO Species VALUE("Twilek",2,964723,2);
INSERT INTO Species VALUE("Wookie",9,116662,2);
INSERT INTO Inhabits VALUE("Oswaft","Hoth");
INSERT INTO Inhabits VALUE("Ugnaught","Hoth");
INSERT INTO Inhabits VALUE("Asari","Hoth");
INSERT INTO Inhabits VALUE("Kamarian","Tatooine");
INSERT INTO Inhabits VALUE("Pterosaur","Proteus");
INSERT INTO Inhabits VALUE("Ruurian","Proteus");
INSERT INTO Inhabits VALUE("Jawa","Proteus");
INSERT INTO Inhabits VALUE("Turian","Nausicaa");
INSERT INTO Inhabits VALUE("Taurill","Circe");
INSERT INTO Inhabits VALUE("Droch","Circe");
INSERT INTO Inhabits VALUE("Ortolan","Circe");
INSERT INTO Inhabits VALUE("Priapulin","Pharos");
INSERT INTO Inhabits VALUE("Dressellian","Pharos");
INSERT INTO Inhabits VALUE("Oswaft","Junthor");
INSERT INTO Inhabits VALUE("Ixll","Pressha");
INSERT INTO Inhabits VALUE("Barabel","Pressha");
INSERT INTO Inhabits VALUE("Drall","Rayingri");
INSERT INTO Inhabits VALUE("Thernbee","Rayingri");
INSERT INTO Inhabits VALUE("Quarren","Sogelrus");
INSERT INTO Inhabits VALUE("Ewok","Sogelrus");
INSERT INTO Inhabits VALUE("Pydyrian","Benda");
INSERT INTO Inhabits VALUE("Bothan","Benda");
INSERT INTO Inhabits VALUE("Sarlacc","Zaherux");
INSERT INTO Inhabits VALUE("Droch","Zaherux");
INSERT INTO Inhabits VALUE("Turian","Hong");
INSERT INTO Inhabits VALUE("Drovian","Hong");
INSERT INTO Inhabits VALUE("Rancor","Casbin");
INSERT INTO Inhabits VALUE("Psadan","Casbin");
INSERT INTO Inhabits VALUE("Sarlacc","Matar");
INSERT INTO Inhabits VALUE("Bantha","Treagir");
INSERT INTO Inhabits VALUE("Rancor","Tereshkova");
INSERT INTO Inhabits VALUE("Oswaft","Tereshkova");
INSERT INTO Inhabits VALUE("Ruurian","Tereshkova");
INSERT INTO Inhabits VALUE("Ithroian","Thegeuse");
INSERT INTO Inhabits VALUE("Thernbee","Solmarlon");
