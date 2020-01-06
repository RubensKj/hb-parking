package br.com.hbparking.vagadegaragem;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SortingVaga {
    private final IVagaGaragemRepository iVagaGaragemRepository;

    public SortingVaga(IVagaGaragemRepository iVagaGaragemRepository) {
        this.iVagaGaragemRepository = iVagaGaragemRepository;
    }

    public List<VagaGaragem> sortingVagas(int qtdVagas) {

        List<VagaGaragem> vagaGaragemList = this.iVagaGaragemRepository.findAll(PageRequest.of(0, qtdVagas)).toList();

        List[] vagasFiltradas = new List[]{
                vagaGaragemList.stream().filter(vagaGaragem -> vagaGaragem.getColaborador().isPcd()).collect(Collectors.toList()),
                vagaGaragemList.stream().filter(vagaGaragem -> LocalDate.now().getYear() - vagaGaragem.getColaborador().getDataNascimento().getYear() == 60).collect(Collectors.toList()),
                vagaGaragemList.stream().filter(vagaGaragem -> vagaGaragem.getColaborador().isResideForaBlumenau()).collect(Collectors.toList())
        };


        List<VagaGaragem> vagasSorteadas = new ArrayList<>();
        for (int i = 0; i < vagasFiltradas.length; i++) {
            vagasSorteadas = this.sortList(vagasFiltradas[i], qtdVagas, vagasSorteadas);
        }
        return vagasSorteadas.stream().distinct().sorted(Comparator.comparing(vagaGaragem -> vagaGaragem.getColaborador().getEmail())).collect(Collectors.toList());
    }

    public List<VagaGaragem> sortList(List<VagaGaragem> filtro, int qtdVagas, List<VagaGaragem> returnedList) {
        for (int i = 0; i < filtro.size(); i++) {
            if (i >= qtdVagas) {
                break;
            }
            returnedList.add(filtro.get(i));
            qtdVagas--;
        }
        return returnedList;
    }
}
