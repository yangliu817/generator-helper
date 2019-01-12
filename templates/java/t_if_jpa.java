            if (query.[getMethod]() != null) {
                Path<[fieldType]> path = root.get("[fieldName]");
                Predicate predicate = criteriaBuilder.equal(path, query.[getMethod]());
                predicates.add(predicate);
            }