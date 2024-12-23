package weshare.api;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import weshare.api.dto.ExpenseDTO;
import weshare.api.dto.NewExpenseDTO;
import weshare.model.Expense;
import weshare.model.Person;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static weshare.model.DateHelper.DD_MM_YYYY;
import static weshare.model.DateHelper.TODAY;

public class ExpenseApiTests extends ApiTestRunner {

    @Test
    @DisplayName("GET /api/expenses")
    public void getAll() {
        List<ExpenseDTO> actual = List.of(Unirest.get("/api/expenses").asObject(ExpenseDTO[].class).getBody());
        assertThat(actual).isNotEmpty();
    }

    @Test
    @DisplayName("GET /api/expenses/{expenseId}")
    public void getExpense() {
        Expense expected = scenario.someExpense();
        ExpenseDTO actual = Unirest.get("/api/expenses/{expenseId}")
                .routeParam("expenseId", expected.getId().toString())
                .asObject(ExpenseDTO.class)
                .getBody();
        assertThat(actual).isEqualTo(ExpenseDTO.fromExpense(expected));
    }

    @Test
    @DisplayName("404 GET /api/expenses/{expenseId}")
    public void getExpense04() {
        int id = scenario.getUnusedExpenseId();
        HttpResponse<JsonNode> response = Unirest.get("/api/expenses/{expenseId}")
                .routeParam("expenseId", Integer.toString(id))
                .asJson();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("POST /api/expenses")
    public void post() {
        Person person = scenario.somePerson();
        NewExpenseDTO newExpenseDTO = new NewExpenseDTO(person.getId(), "Some expense", TODAY.format(DD_MM_YYYY), 100);
        ExpenseDTO expenseDTO = Unirest.post("/api/expenses")
                .body(newExpenseDTO)
                .asObject(ExpenseDTO.class)
                .getBody();
        assertThat(expenseDTO.getExpenseId()).isNotNull();
        assertThat(expenseDTO.getPersonId()).isEqualTo(newExpenseDTO.getPersonId());
        assertThat(expenseDTO.getDescription()).isEqualTo(newExpenseDTO.getDescription());
        assertThat(expenseDTO.getDate()).isEqualTo(newExpenseDTO.getDate());
        assertThat(expenseDTO.getAmount()).isEqualTo(newExpenseDTO.getAmount());
    }

    @Test
    @DisplayName("POST /api/expenses")
    public void post404() {
        Integer id = scenario.getUnusedPersonId();
        NewExpenseDTO newExpenseDTO = new NewExpenseDTO(id, "Some expense", TODAY.format(DD_MM_YYYY), 100);
        HttpResponse<JsonNode> response = Unirest.post("/api/expenses")
                .body(newExpenseDTO)
                .asJson();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("GET /api/expenses/person/{personId}")
    public void getExpenseForPerson() {
        Person person = scenario.somePerson();
        List<ExpenseDTO> actual = List.of(Unirest.get("/api/expenses/person/{personId}")
                        .routeParam("personId", Integer.toString(person.getId()))
                .asObject(ExpenseDTO[].class).getBody());
        assertThat(actual).isNotEmpty();
    }
    @Test
    @DisplayName("404 GET /api/expenses/person/{personId}")
    public void getExpenseForPerson404() {
        int id = scenario.getUnusedPersonId();
        HttpResponse<JsonNode> response = Unirest.get("/api/expenses/person/{personId}")
                .routeParam("personId", Integer.toString(id))
                .asJson();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
