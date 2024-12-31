grammar HexAround;


configuration           : 'CREATURES' ':' creatureDefs+=creatureDefinition+
						  'PLAYERS' ':' playerConfigurations+=playerConfiguration+ EOF ;

creatureDefinition      : creatureName (maxDistance=INTEGER)? '[' properties+=creatureProperty+ ']' ;
creatureName			: BUTTERFLY | CRAB | DOVE | DUCK | GRASSHOPPER | HORSE
						| HUMMINGBIRD | RABBIT | SPIDER | TURTLE ;
creatureProperty		: FLYING | HATCHING | INTRUDING | JUMPING | KAMIKAZE
						| QUEEN | RUNNING | SWAPPING | TRAPPING | WALKING ;

playerConfiguration		: playerName creatures+=creatureList ;
playerName				: RED | BLUE ;
creatureList			: creatureSpecs+=creatureSpecList ;
creatureSpecList		: creatureSpecification (',' creatureSpecification)* ;
creatureSpecification	: creatureName count=INTEGER ;


// Lexical rules

// Creature names
BUTTERFLY 		: 'BUTTERFLY' | 'butterfly' ;
CRAB			: 'CRAB' | 'crab' ;
DOVE			: 'DOVE' | 'dove' ;
DUCK			: 'DUCK' | 'duck' ;
GRASSHOPPER		: 'GRASSHOPPER' | 'grasshopper' ;
HORSE			: 'HORSE' | 'horse' ;
HUMMINGBIRD		: 'HUMMINGBIRD' | 'hummingbird' ;
RABBIT			: 'RABBIT' | 'rabbit' ;
SPIDER			: 'SPIDER' | 'spider' ;
TURTLE			: 'TURTLE' | 'turtle' ;

// Creature properties
FLYING			: 'FLYING' | 'flying' ;
HATCHING		: 'HATCHING' | 'hatching' ;
INTRUDING		: 'INTRUDING' | 'intruding' ;
JUMPING			: 'JUMPING' | 'jumping' ;
KAMIKAZE		: 'KAMIKAZE' | 'kamikaze' ;
QUEEN			: 'QUEEN' | 'queen';
RUNNING			: 'RUNNING' | 'running' ;
SWAPPING		: 'SWAPPING' | 'swapping' ;
TRAPPING		: 'TRAPPING' | 'trapping' ;
WALKING			: 'WALKING' | 'walking' ;

// Player names
BLUE			: 'BLUE' | 'blue' ;
RED				: 'RED' | 'red' ;

WS 				: [ \t\r\n\f]+ -> skip ;
COMMENT  		: '/*' (COMMENT | .)*? '*/' -> skip ;

INTEGER			: DIGIT+ ;
LETTER			: [a-zA-Z] ;

fragment DIGIT	: [0-9] ;
