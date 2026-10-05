

create table if not exists "ingredient"(
    ingredient varchar(50) primary key,
    label varchar(100),
    short_label varchar(50),
    create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by_id BIGINT REFERENCES "user" (user_id),
    updated_by_id BIGINT REFERENCES "user" (user_id)
);

create table if not exists "lk_recipe_type"(
    recipe_type_lk varchar(50) primary key,
    label varchar(100),
    short_label varchar(50),
    create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by_id BIGINT REFERENCES "user" (user_id),
    updated_by_id BIGINT REFERENCES "user" (user_id)
);

insert into "lk_recipe_type" (recipe_type_lk, label, short_label)
values
    ('snack',        'Snack',        'Snack'),
    ('main_course',  'Main Course',  'Main'),
    ('seasoning',    'Seasoning',    'Seasoning'),
    ('breakfast',    'Breakfast',    'Breakfast'),
    ('sauce',        'Sauce',        'Sauce'),
    ('appetizer',    'Appetizer',    'Appetizer'),
    ('dessert',      'Dessert',      'Dessert'),
    ('soup',         'Soup',         'Soup'),
    ('salad',        'Salad',        'Salad'),
    ('side_dish',    'Side Dish',    'Side'),
    ('drink',        'Drink',        'Drink'),
    ('bread',        'Bread',        'Bread');

create table if not exists "recipe"(
    recipe_id bigserial primary key,
    name varchar(100) not null,
    footnote text,
    description text,
    source_url text,
    servings smallint not null default 0,
    rating smallint not null default 0,
    prep_time int not null default 0,
    cook_time int not null default 0, 
    calories int,
    is_published boolean not null default false,
    recipe_type_lk varchar(50) references "lk_recipe_type" (recipe_type_lk),
    published_date TIMESTAMPTZ,
    create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by_id BIGINT REFERENCES "user" (user_id),
    updated_by_id BIGINT REFERENCES "user" (user_id)
);

create table if not exists "recipe_ingredient"(
    recipe_ingredient_id bigserial primary key,
    recipe_id bigint REFERENCES "recipe" (recipe_id),
    ingredient varchar(50) references "ingredient" (ingredient),
    amount varchar(50),
    create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by_id BIGINT REFERENCES "user" (user_id),
    updated_by_id BIGINT REFERENCES "user" (user_id)
);

create table if not exists "recipe_step"(
    recipe_step_id bigserial primary key,
    recipe_id bigint REFERENCES "recipe" (recipe_id),
    step_order int,
    footnote TEXT,
    step_text TEXT,
    create_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by_id BIGINT REFERENCES "user" (user_id),
    updated_by_id BIGINT REFERENCES "user" (user_id)
)