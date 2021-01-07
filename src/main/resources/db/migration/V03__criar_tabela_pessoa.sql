create table pessoa(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL,
	nome VARCHAR(50) NOT NULL,
	ativo BOOLEAN NOT NULL,
	logradouro VARCHAR(50),
	numero VARCHAR(50),
	complemento VARCHAR(50),
	bairro VARCHAR(50),
	cep VARCHAR(50),
	cidade VARCHAR(50),
	estado VARCHAR(50) 	
)ENGINE=InnoDB CHARSET=utf8;

INSERT into pessoa values (1,'Victor André', true, "Rua Plutão", "58", "casa", "Bela Vista", "07133320", "Guarulhos", "SP");