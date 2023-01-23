SELECT posts.*,
       sum(category) as rating,
       count(category) filter ( where category=0 ) as dead_count,
       array_agg('[' || images.id || ',' || images.alt || ']') as images,
       (SELECT category from ratings r WHERE  r.post_id = posts.id AND r.student_id = $1) as users_rating
FROM posts
    LEFT JOIN ratings ON (posts.id = ratings.post_id)
    LEFT JOIN images on posts.id = images.post_id
WHERE (posts.flag = true)
GROUP BY posts.id
having count(category) filter ( where category=0 ) < 7 --const  or rations ?