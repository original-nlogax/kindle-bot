// don't forget to leave ; at the end

merge into messages(id, name, description, text)
    values(
        0,
        'START_MESSAGE',
        'Start',
        'Привет!'
    );

merge into admins(chat_id, username) values (6357726426, 'vienulle');
