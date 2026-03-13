-- Oracle seed script for Defis + DefiQuestion
-- Safe to rerun: each Defi is upserted and its questions are replaced.
-- Run example: sqlplus user/password@ORCL @src/main/resources/sql/seed_defis.sql

SET DEFINE OFF;

DECLARE
    v_defi_id NUMBER;
BEGIN
    MERGE INTO defi d
    USING (SELECT 'Culture Generale Express' AS titre FROM dual) s
    ON (d.titre = s.titre)
    WHEN MATCHED THEN
        UPDATE SET
            d.description = '6 questions variees pour demarrer la journee.',
            d.start_date = TRUNC(SYSDATE) - 1,
            d.end_date = TRUNC(SYSDATE) + 365,
            d.active = 1
    WHEN NOT MATCHED THEN
        INSERT (id, titre, description, start_date, end_date, active)
        VALUES (defi_seq.NEXTVAL, s.titre, '6 questions variees pour demarrer la journee.', TRUNC(SYSDATE) - 1, TRUNC(SYSDATE) + 365, 1);

    SELECT id INTO v_defi_id FROM defi WHERE titre = 'Culture Generale Express';
    DELETE FROM defi_question WHERE defi_id = v_defi_id;

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 1, 'Qui a peint la Joconde ?', 'Claude Monet', 'Leonard de Vinci', 'Vincent van Gogh', 'Pablo Picasso', 'B', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 2, 'Combien y a-t-il de continents ?', '5', '6', '7', '8', 'C', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 3, 'Quelle est la monnaie officielle du Japon ?', 'Yuan', 'Won', 'Yen', 'Dollar', 'C', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 4, 'Quel ocean borde la cote ouest de l''Afrique ?', 'Ocean Pacifique', 'Ocean Indien', 'Ocean Arctique', 'Ocean Atlantique', 'D', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 5, 'En quelle annee a eu lieu la chute du mur de Berlin ?', '1987', '1989', '1991', '1993', 'B', 3);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 6, 'Quel est le symbole chimique de l''or ?', 'Ag', 'Au', 'Fe', 'Go', 'B', 3);
END;
/

DECLARE
    v_defi_id NUMBER;
BEGIN
    MERGE INTO defi d
    USING (SELECT 'Sciences et Tech' AS titre FROM dual) s
    ON (d.titre = s.titre)
    WHEN MATCHED THEN
        UPDATE SET
            d.description = 'Defi autour de la science, du web et de l''informatique.',
            d.start_date = TRUNC(SYSDATE) - 1,
            d.end_date = TRUNC(SYSDATE) + 365,
            d.active = 1
    WHEN NOT MATCHED THEN
        INSERT (id, titre, description, start_date, end_date, active)
        VALUES (defi_seq.NEXTVAL, s.titre, 'Defi autour de la science, du web et de l''informatique.', TRUNC(SYSDATE) - 1, TRUNC(SYSDATE) + 365, 1);

    SELECT id INTO v_defi_id FROM defi WHERE titre = 'Sciences et Tech';
    DELETE FROM defi_question WHERE defi_id = v_defi_id;

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 1, 'Quelle planete est surnommee la planete rouge ?', 'Venus', 'Mars', 'Jupiter', 'Saturne', 'B', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 2, 'Quel langage est principalement utilise avec Spring Boot ?', 'Python', 'C#', 'Java', 'Ruby', 'C', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 3, 'Que signifie HTTP ?', 'HyperText Transfer Protocol', 'High Transmission Text Process', 'Hyperlink Transfer Program', 'Host Tunnel Transport Protocol', 'A', 3);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 4, 'Dans une base relationnelle, SQL sert a ...', 'dessiner des interfaces', 'manipuler des donnees', 'compiler du Java', 'gerer les DNS', 'B', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 5, 'Lequel est un systeme de controle de version ?', 'Docker', 'Git', 'Kubernetes', 'Maven', 'B', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 6, 'Quel est le role principal de CSS ?', 'Styler les pages web', 'Stocker des donnees', 'Chiffrer des mots de passe', 'Executer des tests unitaires', 'A', 3);
END;
/

DECLARE
    v_defi_id NUMBER;
BEGIN
    MERGE INTO defi d
    USING (SELECT 'Geographie Monde' AS titre FROM dual) s
    ON (d.titre = s.titre)
    WHEN MATCHED THEN
        UPDATE SET
            d.description = 'Questions sur les capitales, reliefs et regions du monde.',
            d.start_date = TRUNC(SYSDATE) - 1,
            d.end_date = TRUNC(SYSDATE) + 365,
            d.active = 1
    WHEN NOT MATCHED THEN
        INSERT (id, titre, description, start_date, end_date, active)
        VALUES (defi_seq.NEXTVAL, s.titre, 'Questions sur les capitales, reliefs et regions du monde.', TRUNC(SYSDATE) - 1, TRUNC(SYSDATE) + 365, 1);

    SELECT id INTO v_defi_id FROM defi WHERE titre = 'Geographie Monde';
    DELETE FROM defi_question WHERE defi_id = v_defi_id;

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 1, 'Quelle est la capitale du Canada ?', 'Toronto', 'Vancouver', 'Ottawa', 'Montreal', 'C', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 2, 'Quel est le plus grand desert chaud du monde ?', 'Gobi', 'Kalahari', 'Atacama', 'Sahara', 'D', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 3, 'Quel fleuve traverse Le Caire ?', 'Le Nil', 'Le Danube', 'L''Amazone', 'Le Yangzi Jiang', 'A', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 4, 'Dans quel pays se trouve la ville de Cusco ?', 'Mexique', 'Perou', 'Chili', 'Bolivie', 'B', 3);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 5, 'Quel est le plus haut sommet du monde ?', 'Mont Kilimandjaro', 'Mont Blanc', 'K2', 'Everest', 'D', 2);

    INSERT INTO defi_question (id, defi_id, ordre_question, question_text, option_a, option_b, option_c, option_d, correct_option, points)
    VALUES (defi_question_seq.NEXTVAL, v_defi_id, 6, 'Quel pays est traverse par l''equateur ET le meridien de Greenwich ?', 'Gabon', 'Congo', 'Ghana', 'Sao Tome-et-Principe', 'C', 4);
END;
/

COMMIT;

-- Quick checks:
SELECT id, titre, active, start_date, end_date FROM defi ORDER BY id;
SELECT defi_id, COUNT(*) AS nb_questions FROM defi_question GROUP BY defi_id ORDER BY defi_id;
