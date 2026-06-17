package com.lela;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MapperGenerator {

    static final String BASE_PACKAGE = "com.lela";
    static final String BASE_PATH = "src/main/java/com/lela/";

    public static void main(String[] args) throws Exception {
        Class<?>[] entities = {
                Class.forName("com.lela.deckenrollment.DeckEnrollment"),
                Class.forName("com.lela.cardprogress.CardProgress"),
                Class.forName("com.lela.reviewsession.ReviewSession"),
                Class.forName("com.lela.srsreview.SrsReview"),
                Class.forName("com.lela.dailylearningactivity.DailyLearningActivity"),
                Class.forName("com.lela.leaderboardsnapshot.LeaderboardSnapshot"),
                Class.forName("com.lela.notification.Notification"),
                Class.forName("com.lela.payment.Payment"),
                Class.forName("com.lela.usersubscription.UserSubscription")
        };

        for (Class<?> clazz : entities) {
            String entityName = clazz.getSimpleName();
            String featurePkg = entityName.toLowerCase();
            String featurePath = BASE_PATH + featurePkg + "/";

            List<Field> allFields = new ArrayList<>();
            Class<?> current = clazz;
            while (current != null && current != Object.class) {
                for (Field f : current.getDeclaredFields()) {
                    if (!Modifier.isStatic(f.getModifiers())) {
                        allFields.add(f);
                    }
                }
                current = current.getSuperclass();
            }

            StringBuilder reqFields = new StringBuilder();
            StringBuilder resFields = new StringBuilder();
            StringBuilder mapToEntity = new StringBuilder();
            StringBuilder mapToResponse = new StringBuilder();
            
            for (Field f : allFields) {
                String name = f.getName();
                if (name.equals("id") || name.equals("createdAt") || name.equals("updatedAt") || name.equals("deletedAt") || name.equals("version")) {
                    if (name.equals("id")) {
                        resFields.append("    private Long id;\n");
                        mapToResponse.append("        response.setId(entity.getId());\n");
                    } else if (name.equals("createdAt")) {
                        resFields.append("    private LocalDateTime createdAt;\n");
                        mapToResponse.append("        response.setCreatedAt(entity.getCreatedAt());\n");
                    } else if (name.equals("updatedAt")) {
                        resFields.append("    private LocalDateTime updatedAt;\n");
                        mapToResponse.append("        response.setUpdatedAt(entity.getUpdatedAt());\n");
                    } else if (name.equals("deletedAt")) {
                        resFields.append("    private LocalDateTime deletedAt;\n");
                        mapToResponse.append("        response.setDeletedAt(entity.getDeletedAt());\n");
                    } else if (false) {
                        resFields.append("    private Long id;\n");
                        mapToResponse.append("        response.setId(entity.getId());\n");
                    }
                    continue;
                }
                String type = f.getType().getSimpleName();
                
                boolean isRelation = false;
                // Basic heuristic for relational entities (Users, Deck, Flashcard, SubscriptionPlan, ReviewSession, etc.)
                if (f.getType().getName().startsWith("com.lela.domain.entity.") || f.getType().getName().startsWith("com.lela.")) {
                    if (!f.getType().isEnum()) {
                        isRelation = true;
                        type = "Long";
                        name = name + "Id";
                        
                        String relName = f.getName();
                        
                        // We will add a comment to map the relation
                        mapToEntity.append("        // TODO: Map relation '").append(relName).append("' using '").append(name).append("'. E.g. entity.set").append(capitalize(relName)).append("(repository.findById(request.get").append(capitalize(name)).append("()).orElseThrow());\n");
                    }
                }

                reqFields.append("    private ").append(type).append(" ").append(name).append(";\n");
                resFields.append("    private ").append(type).append(" ").append(name).append(";\n");

                if (!isRelation) {
                    mapToEntity.append("        entity.set").append(capitalize(f.getName())).append("(request.get").append(capitalize(name)).append("());\n");
                    mapToResponse.append("        response.set").append(capitalize(name)).append("(entity.get").append(capitalize(f.getName())).append("());\n");
                } else {
                    mapToResponse.append("        if (entity.get").append(capitalize(f.getName())).append("() != null) {\n");
                    mapToResponse.append("            response.set").append(capitalize(name)).append("(entity.get").append(capitalize(f.getName())).append("().getId());\n");
                    mapToResponse.append("        }\n");
                }
            }

            // Replace Enums string mapping with correct packages if necessary, but DTO can just use Enum types if we add import com.lela.domain.enums.*;
            String reqDto = "package " + BASE_PACKAGE + "." + featurePkg + ".dto;\n\n" +
                    "import lombok.Getter;\n" +
                    "import lombok.Setter;\n" +
                    "import jakarta.validation.constraints.*;\n" +
                    "import java.time.LocalDateTime;\n" +
                    "import java.time.LocalDate;\n" +
                    "import java.math.BigDecimal;\n" +
                    "import com.lela.domain.enums.*;\n\n" +
                    "@Getter\n" +
                    "@Setter\n" +
                    "public class " + entityName + "Request {\n" +
                    reqFields.toString() +
                    "}\n";
            Files.write(Paths.get(featurePath + "dto/" + entityName + "Request.java"), reqDto.getBytes());

            String resDto = "package " + BASE_PACKAGE + "." + featurePkg + ".dto;\n\n" +
                    "import lombok.Getter;\n" +
                    "import lombok.Setter;\n" +
                    "import java.time.LocalDateTime;\n" +
                    "import java.time.LocalDate;\n" +
                    "import java.math.BigDecimal;\n" +
                    "import com.lela.domain.enums.*;\n\n" +
                    "@Getter\n" +
                    "@Setter\n" +
                    "public class " + entityName + "Response {\n" +
                    resFields.toString() +
                    "}\n";
            Files.write(Paths.get(featurePath + "dto/" + entityName + "Response.java"), resDto.getBytes());

            String serviceImpl = "package " + BASE_PACKAGE + "." + featurePkg + ".service.impl;\n\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + "." + entityName + ";\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".dto." + entityName + "Request;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".dto." + entityName + "Response;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".repository." + entityName + "Repository;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".service." + entityName + "Service;\n" +
                    "import " + BASE_PACKAGE + ".common.exception.NotFoundExeception;\n" +
                    "import lombok.RequiredArgsConstructor;\n" +
                    "import org.springframework.data.domain.Page;\n" +
                    "import org.springframework.data.domain.Pageable;\n" +
                    "import org.springframework.stereotype.Service;\n" +
                    "import org.springframework.transaction.annotation.Transactional;\n\n" +
                    "@Service\n" +
                    "@RequiredArgsConstructor\n" +
                    "public class " + entityName + "ServiceImpl implements " + entityName + "Service {\n\n" +
                    "    private final " + entityName + "Repository repository;\n\n" +
                    "    @Override\n" +
                    "    public Page<" + entityName + "Response> getAll(Pageable pageable) {\n" +
                    "        return repository.findAll(pageable).map(this::mapToResponse);\n" +
                    "    }\n\n" +
                    "    @Override\n" +
                    "    public " + entityName + "Response getById(Long id) {\n" +
                    "        " + entityName + " entity = repository.findById(id)\n" +
                    "                .orElseThrow(() -> new NotFoundExeception(\"" + entityName + " not found with id: \" + id));\n" +
                    "        return mapToResponse(entity);\n" +
                    "    }\n\n" +
                    "    @Override\n" +
                    "    @Transactional\n" +
                    "    public " + entityName + "Response create(" + entityName + "Request request) {\n" +
                    "        " + entityName + " entity = new " + entityName + "();\n" +
                    mapToEntity.toString() +
                    "        return mapToResponse(repository.save(entity));\n" +
                    "    }\n\n" +
                    "    @Override\n" +
                    "    @Transactional\n" +
                    "    public " + entityName + "Response update(Long id, " + entityName + "Request request) {\n" +
                    "        " + entityName + " entity = repository.findById(id)\n" +
                    "                .orElseThrow(() -> new NotFoundExeception(\"" + entityName + " not found with id: \" + id));\n" +
                    mapToEntity.toString() +
                    "        return mapToResponse(repository.save(entity));\n" +
                    "    }\n\n" +
                    "    @Override\n" +
                    "    @Transactional\n" +
                    "    public void delete(Long id) {\n" +
                    "        if (!repository.existsById(id)) {\n" +
                    "            throw new NotFoundExeception(\"" + entityName + " not found with id: \" + id);\n" +
                    "        }\n" +
                    "        repository.deleteById(id);\n" +
                    "    }\n\n" +
                    "    private " + entityName + "Response mapToResponse(" + entityName + " entity) {\n" +
                    "        " + entityName + "Response response = new " + entityName + "Response();\n" +
                    mapToResponse.toString() +
                    "        return response;\n" +
                    "    }\n" +
                    "}\n";
            Files.write(Paths.get(featurePath + "service/impl/" + entityName + "ServiceImpl.java"), serviceImpl.getBytes());
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}


