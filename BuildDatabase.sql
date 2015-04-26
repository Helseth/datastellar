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
INSERT INTO Star VALUE("Sol",9999,"Yellow Giant","Milky Way");
INSERT INTO Planet VALUE("Earth",999,"Sol",1000,6000000000,"Milky Way");
INSERT INTO Moon VALUE("Luna",100,"Earth",10,"Milky Way");
INSERT INTO Species VALUE("Human",6,6000000000,1);
