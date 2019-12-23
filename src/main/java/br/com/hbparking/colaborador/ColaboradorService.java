package br.com.hbparking.colaborador;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class ColaboradorService {
    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    public void save(ColaboradorDTO colaboradorDTO){
        Colaborador colaborador = new Colaborador();
        colaborador.setDataNascimento(convertStringToCalendar(colaboradorDTO.getDataNascimento()));
        colaborador.setEmail(colaboradorDTO.getEmail());
        colaborador.setNome(colaboradorDTO.getNome());
        colaborador.setPcd(colaboradorDTO.isPcd());
        colaborador.setTrabalhoNoturno(colaboradorDTO.isTrabalhoNoturno());

        colaborador = this.colaboradorRepository.save(colaborador);
    }

    public Calendar convertStringToCalendar(String dataNascimento){
            Calendar calendar = Calendar.getInstance();

            //getting informador from string
            String[] data = dataNascimento.split("-");

            int year  = Integer.parseInt(data[0]);
            int month = Integer.parseInt(data[1]);
            int day   = Integer.parseInt(data[2]);

            calendar.set(year, month, day);

            return calendar;
    }

    public Colaborador getEntityById(int id){
        Optional<Colaborador> colaboradorOptional = this.colaboradorRepository.findById(id);

        if(colaboradorOptional.isPresent()){
            return colaboradorOptional.get();
        }
        throw new IllegalArgumentException(String.format("O colaborador informado(%s) não existe", id));
    }

    public void delete(int id){
        this.colaboradorRepository.deleteById(id);
    }

    public ColaboradorDTO update(ColaboradorDTO colaboradorDTO ,int id){
        Colaborador colaborador = new Colaborador();
        colaborador.setDataNascimento(convertStringToCalendar(colaboradorDTO.getDataNascimento()));
        colaborador.setEmail(colaboradorDTO.getEmail());
        colaborador.setNome(colaboradorDTO.getNome());
        colaborador.setPcd(colaboradorDTO.isPcd());
        colaborador.setTrabalhoNoturno(colaboradorDTO.isTrabalhoNoturno());

        colaborador = this.colaboradorRepository.save(colaborador);

        return ColaboradorDTO.of(colaborador);
    }

    public ColaboradorDTO getColaborador(int id){
        Optional<Colaborador> colaboradorOptional = this.colaboradorRepository.findById(id);

        if(colaboradorOptional.isPresent()){
            return ColaboradorDTO.of(colaboradorOptional.get());
        }
        throw new IllegalArgumentException(String.format("O colaborador informado(%s) não existe.", id));
    }
}
