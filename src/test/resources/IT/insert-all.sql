INSERT INTO CLIENTE(id_externo, nome) VALUES ('7680a4b2-0a39-4185-82f9-ba5a02fd917d', 'João Victor');
INSERT INTO CLIENTE(id_externo, nome) VALUES ('cead719d-91d9-4a65-bf56-77bb528cf60b', 'Kalina');
INSERT INTO CLIENTE(id_externo, nome) VALUES ('1891e29e-b3e0-4a4c-8b3b-5f7bc0218bd3', 'Miguel');
INSERT INTO CLIENTE(id_externo, nome) VALUES ('e29c1be9-29f6-4e56-b28a-00b13ee575cf', 'Mel');

INSERT INTO PROJETO(id_externo, descricao, data_hora_criacao, status) VALUES ('93bae4f2-7002-4b8b-b913-737d44ff5b4b', 'Desafio técnico', '2025-03-04 16:30:00', 'ANDAMENTO');
INSERT INTO PROJETO(id_externo, descricao, data_hora_criacao, status) VALUES ('baf0640d-c563-4465-b3a0-41fc8240a100', 'Criar aplicações mordenas', '2025-03-04 16:30:00', 'ANDAMENTO');

INSERT INTO ATIVIDADE(id_externo, titulo, descricao, data_hora_criacao, status, atividade_id, projeto_id) VALUES ('40e691e8-929c-4371-9f32-f8bf09835b74', 'resolver desafio técnico', 'criar uma API completa', '2025-03-04 16:30:00', 'DESENVOLVIMENTO', 1, 1);

INSERT INTO PROJETOS_PARTICIPANTES(PROJETO_ID, CLIENTE_ID) VALUES (1, 1);
INSERT INTO PROJETOS_PARTICIPANTES(PROJETO_ID, CLIENTE_ID) VALUES (2, 1);