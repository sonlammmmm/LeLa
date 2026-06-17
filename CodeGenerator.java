import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CodeGenerator {

    static final String BASE_PACKAGE = "com.lela";
    static final String BASE_PATH = "src/main/java/com/lela/";

    public static void main(String[] args) throws IOException {
        List<String> entities = Arrays.asList(
                "DeckEnrollment", "CardProgress", "ReviewSession", "SrsReview", "DailyLearningActivity",
                "LeaderboardSnapshot", "Notification", "Payment", "UserSubscription"
        );

        for (String entity : entities) {
            String featurePkg = entity.toLowerCase();
            String featurePath = BASE_PATH + featurePkg + "/";

            new File(featurePath + "dto").mkdirs();
            new File(featurePath + "repository").mkdirs();
            new File(featurePath + "service/impl").mkdirs();
            new File(featurePath + "controller").mkdirs();

            String reqDto = "package " + BASE_PACKAGE + "." + featurePkg + ".dto;\n\n" +
                    "import lombok.Getter;\n" +
                    "import lombok.Setter;\n\n" +
                    "@Getter\n" +
                    "@Setter\n" +
                    "public class " + entity + "Request {\n" +
                    "    // TODO: Add fields based on entity\n" +
                    "}\n";
            Files.write(Paths.get(featurePath + "dto/" + entity + "Request.java"), reqDto.getBytes());

            String resDto = "package " + BASE_PACKAGE + "." + featurePkg + ".dto;\n\n" +
                    "import lombok.Getter;\n" +
                    "import lombok.Setter;\n\n" +
                    "@Getter\n" +
                    "@Setter\n" +
                    "public class " + entity + "Response {\n" +
                    "    private Long id;\n" +
                    "    // TODO: Add fields based on entity\n" +
                    "}\n";
            Files.write(Paths.get(featurePath + "dto/" + entity + "Response.java"), resDto.getBytes());

            String repo = "package " + BASE_PACKAGE + "." + featurePkg + ".repository;\n\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + "." + entity + ";\n" +
                    "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                    "import org.springframework.stereotype.Repository;\n\n" +
                    "@Repository\n" +
                    "public interface " + entity + "Repository extends JpaRepository<" + entity + ", Long> {\n" +
                    "}\n";
            Files.write(Paths.get(featurePath + "repository/" + entity + "Repository.java"), repo.getBytes());

            String service = "package " + BASE_PACKAGE + "." + featurePkg + ".service;\n\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".dto." + entity + "Request;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".dto." + entity + "Response;\n" +
                    "import org.springframework.data.domain.Page;\n" +
                    "import org.springframework.data.domain.Pageable;\n\n" +
                    "public interface " + entity + "Service {\n" +
                    "    Page<" + entity + "Response> getAll(Pageable pageable);\n" +
                    "    " + entity + "Response getById(Long id);\n" +
                    "    " + entity + "Response create(" + entity + "Request request);\n" +
                    "    " + entity + "Response update(Long id, " + entity + "Request request);\n" +
                    "    void delete(Long id);\n" +
                    "}\n";
            Files.write(Paths.get(featurePath + "service/" + entity + "Service.java"), service.getBytes());

            String serviceImpl = "package " + BASE_PACKAGE + "." + featurePkg + ".service.impl;\n\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + "." + entity + ";\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".dto." + entity + "Request;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".dto." + entity + "Response;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".repository." + entity + "Repository;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".service." + entity + "Service;\n" +
                    "import " + BASE_PACKAGE + ".common.exception.NotFoundExeception;\n" +
                    "import lombok.RequiredArgsConstructor;\n" +
                    "import org.springframework.data.domain.Page;\n" +
                    "import org.springframework.data.domain.Pageable;\n" +
                    "import org.springframework.stereotype.Service;\n" +
                    "import org.springframework.transaction.annotation.Transactional;\n\n" +
                    "@Service\n" +
                    "@RequiredArgsConstructor\n" +
                    "public class " + entity + "ServiceImpl implements " + entity + "Service {\n\n" +
                    "    private final " + entity + "Repository repository;\n\n" +
                    "    @Override\n" +
                    "    public Page<" + entity + "Response> getAll(Pageable pageable) {\n" +
                    "        return repository.findAll(pageable).map(this::mapToResponse);\n" +
                    "    }\n\n" +
                    "    @Override\n" +
                    "    public " + entity + "Response getById(Long id) {\n" +
                    "        " + entity + " entity = repository.findById(id)\n" +
                    "                .orElseThrow(() -> new NotFoundExeception(\"" + entity + " not found with id: \" + id));\n" +
                    "        return mapToResponse(entity);\n" +
                    "    }\n\n" +
                    "    @Override\n" +
                    "    @Transactional\n" +
                    "    public " + entity + "Response create(" + entity + "Request request) {\n" +
                    "        " + entity + " entity = new " + entity + "();\n" +
                    "        // TODO: map request to entity\n" +
                    "        return mapToResponse(repository.save(entity));\n" +
                    "    }\n\n" +
                    "    @Override\n" +
                    "    @Transactional\n" +
                    "    public " + entity + "Response update(Long id, " + entity + "Request request) {\n" +
                    "        " + entity + " entity = repository.findById(id)\n" +
                    "                .orElseThrow(() -> new NotFoundExeception(\"" + entity + " not found with id: \" + id));\n" +
                    "        // TODO: map request to entity\n" +
                    "        return mapToResponse(repository.save(entity));\n" +
                    "    }\n\n" +
                    "    @Override\n" +
                    "    @Transactional\n" +
                    "    public void delete(Long id) {\n" +
                    "        if (!repository.existsById(id)) {\n" +
                    "            throw new NotFoundExeception(\"" + entity + " not found with id: \" + id);\n" +
                    "        }\n" +
                    "        repository.deleteById(id);\n" +
                    "    }\n\n" +
                    "    private " + entity + "Response mapToResponse(" + entity + " entity) {\n" +
                    "        " + entity + "Response response = new " + entity + "Response();\n" +
                    "        response.setId(entity.getId());\n" +
                    "        return response;\n" +
                    "    }\n" +
                    "}\n";
            Files.write(Paths.get(featurePath + "service/impl/" + entity + "ServiceImpl.java"), serviceImpl.getBytes());

            String mapping = entity.toLowerCase() + "s";
            String controller = "package " + BASE_PACKAGE + "." + featurePkg + ".controller;\n\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".dto." + entity + "Request;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".dto." + entity + "Response;\n" +
                    "import " + BASE_PACKAGE + "." + featurePkg + ".service." + entity + "Service;\n" +
                    "import " + BASE_PACKAGE + ".common.ApiResponse;\n" +
                    "import lombok.RequiredArgsConstructor;\n" +
                    "import org.springframework.data.domain.Page;\n" +
                    "import org.springframework.data.domain.Pageable;\n" +
                    "import org.springframework.http.HttpStatus;\n" +
                    "import org.springframework.web.bind.annotation.*;\n\n" +
                    "@RestController\n" +
                    "@RequestMapping(\"/api/v1/" + mapping + "\")\n" +
                    "@RequiredArgsConstructor\n" +
                    "public class " + entity + "Controller {\n\n" +
                    "    private final " + entity + "Service service;\n\n" +
                    "    @GetMapping\n" +
                    "    public ApiResponse<Page<" + entity + "Response>> getAll(Pageable pageable) {\n" +
                    "        return ApiResponse.<Page<" + entity + "Response>>builder()\n" +
                    "                .code(HttpStatus.OK.value())\n" +
                    "                .message(\"Success\")\n" +
                    "                .data(service.getAll(pageable))\n" +
                    "                .build();\n" +
                    "    }\n\n" +
                    "    @GetMapping(\"/{id}\")\n" +
                    "    public ApiResponse<" + entity + "Response> getById(@PathVariable Long id) {\n" +
                    "        return ApiResponse.<" + entity + "Response>builder()\n" +
                    "                .code(HttpStatus.OK.value())\n" +
                    "                .message(\"Success\")\n" +
                    "                .data(service.getById(id))\n" +
                    "                .build();\n" +
                    "    }\n\n" +
                    "    @PostMapping\n" +
                    "    public ApiResponse<" + entity + "Response> create(@RequestBody " + entity + "Request request) {\n" +
                    "        return ApiResponse.<" + entity + "Response>builder()\n" +
                    "                .code(HttpStatus.CREATED.value())\n" +
                    "                .message(\"Created\")\n" +
                    "                .data(service.create(request))\n" +
                    "                .build();\n" +
                    "    }\n\n" +
                    "    @PutMapping(\"/{id}\")\n" +
                    "    public ApiResponse<" + entity + "Response> update(@PathVariable Long id, @RequestBody " + entity + "Request request) {\n" +
                    "        return ApiResponse.<" + entity + "Response>builder()\n" +
                    "                .code(HttpStatus.OK.value())\n" +
                    "                .message(\"Updated\")\n" +
                    "                .data(service.update(id, request))\n" +
                    "                .build();\n" +
                    "    }\n\n" +
                    "    @DeleteMapping(\"/{id}\")\n" +
                    "    public ApiResponse<Void> delete(@PathVariable Long id) {\n" +
                    "        service.delete(id);\n" +
                    "        return ApiResponse.<Void>builder()\n" +
                    "                .code(HttpStatus.OK.value())\n" +
                    "                .message(\"Deleted\")\n" +
                    "                .build();\n" +
                    "    }\n" +
                    "}\n";
            Files.write(Paths.get(featurePath + "controller/" + entity + "Controller.java"), controller.getBytes());
        }
    }
}

